package dev.mayurshingare.ucompare;

import static j2html.TagCreator.*;

import j2html.tags.specialized.BodyTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Differentiator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Differentiator.class);

    public static List<Diff> diff(List<FileInfo> expected, List<FileInfo> actual, BiPredicate<FileInfo,FileInfo> comparator){

        final Map<String,FileInfo> originalFiles = expected.stream().collect(Collectors.toMap(FileInfo::getFilePath, Function.identity()));
        final Map<String,FileInfo> compareFiles = actual.stream().collect(Collectors.toMap(FileInfo::getFilePath, Function.identity()));

        List<Diff> diffs = new ArrayList<>();

        for (Map.Entry<String, FileInfo> entry : originalFiles.entrySet()) {
            String path = entry.getKey();
            FileInfo expectedFileInfo = entry.getValue();
            Diff diff;
            if (compareFiles.containsKey(path)) {
                FileInfo actualFileInfo = compareFiles.remove(path);
                if(comparator.test(expectedFileInfo,actualFileInfo)){
                    diff = new Diff(Diff.Type.EQUAL,expectedFileInfo);
                } else {
                    diff = new Diff(Diff.Type.CHANGE,expectedFileInfo);
                }
            } else {
                diff=new Diff(Diff.Type.INSERT,expectedFileInfo);
            }
            LOGGER.debug("{}",diff);
            diffs.add(diff);
        }
        String[] pendingFiles = compareFiles.keySet().toArray(String[]::new);
        for(String path:pendingFiles){
            FileInfo info = compareFiles.remove(path);
            Diff diff = new Diff(Diff.Type.DELETE,info);
            LOGGER.debug("{}",diff);
            diffs.add(diff);
        }
        return diffs;
    }

    public static String diff2HTML(List<Diff> diffs){
        return  html(
                    head(
                        link().withRel("stylesheet").withHref("https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"),
                        link().withRel("stylesheet").withHref("https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css"),
                        link().withRel("stylesheet").withHref("https://unpkg.com/bootstrap-table@1.19.1/dist/bootstrap-table.min.css"),
                        script().withSrc("https://code.jquery.com/jquery-3.6.0.slim.min.js"),
                        script().withSrc("https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"),
                        script().withSrc("https://unpkg.com/bootstrap-table@1.19.1/dist/bootstrap-table.min.js"),
                        script().withSrc("https://unpkg.com/bootstrap-table@1.19.1/dist/extensions/filter-control/bootstrap-table-filter-control.min.js")
                    ),
                    body(
                            table(
                                    thead(tr(th("Left").withData("field","left").withData("filter-control","input"), th("Difference").withData("field","diff").withData("filter-control","select"), th("Right").withData("field","right").withData("filter-control","input"))),
                                    tbody(
                                            each(diffs, diff->
                                                switch(diff.getType()){
                                                    case EQUAL -> tr(
                                                            td(iffElse(diff.getData().isDirectory,span().withClass("glyphicon glyphicon-folder-open"),span().withClass("glyphicon glyphicon-file")),span(diff.getData().filePath)),
                                                            td("EQUAL"),
                                                            td(iffElse(diff.getData().isDirectory,span().withClass("glyphicon glyphicon-folder-open"),span().withClass("glyphicon glyphicon-file")),span(diff.getData().filePath))).withClass("success");
                                                    case INSERT -> tr(
                                                            td(iffElse(diff.getData().isDirectory,span().withClass("glyphicon glyphicon-folder-open"),span().withClass("glyphicon glyphicon-file")),span(diff.getData().filePath)),
                                                            td("ADD"),
                                                            td("<File Missing>")).withClass("info");
                                                    case DELETE -> tr(
                                                            td(""),
                                                            td("DELETE"),
                                                            td(iffElse(diff.getData().isDirectory,span().withClass("glyphicon glyphicon-folder-open"),span().withClass("glyphicon glyphicon-file")),span(diff.getData().filePath))).withClass("danger");
                                                    case CHANGE -> tr(
                                                            td(iffElse(diff.getData().isDirectory,span().withClass("glyphicon glyphicon-folder-open"),span().withClass("glyphicon glyphicon-file")),span(diff.getData().filePath)),
                                                            td("UPDATE"),
                                                            td(iffElse(diff.getData().isDirectory,span().withClass("glyphicon glyphicon-folder-open"),span().withClass("glyphicon glyphicon-file")),span(diff.getData().filePath))).withClass("warning");
                                                }
                                            )
                                    )
                            ).withId("diffTable").withClass("table table-hover table-condensed").withData("filter-control","true"),
                            script("$(function(){$('#diffTable').bootstrapTable()})")
                    )
        ).renderFormatted();
    }
    
    
}
