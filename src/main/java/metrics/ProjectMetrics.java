package main.java.metrics;

import main.java.properties.ProjectProperties;
import main.java.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMetrics {

    private final ProjectProperties p;
    private List<PackageMetrics> packageMetricsList;

    public ProjectMetrics(String root, ProjectProperties projectProperties) throws IOException {
        this.p = projectProperties;
        this.packageMetricsList = getPackageMetricsFromRoot(root);
    }

    private List<PackageMetrics> getPackageMetricsFromRoot(String root) throws IOException {
        File[] files = new File(root).listFiles();
        List<String> dirList = getSubFoldersFromFiles(files, root);
        PackageMetrics packageMetrics = new PackageMetrics(root, p);
        packageMetricsList.add(packageMetrics);

        if(dirList.size() > 0){
            for(String dir : dirList){
                packageMetricsList.addAll(getPackageMetricsFromRoot(dir));
            }
        }

        return packageMetricsList;
    }

    //Liste des sous-dossiers
    //Source: https://www.baeldung.com/java-list-directory-files
    private List<String> getSubFoldersFromFiles(File[] files, String root){
        return Arrays.stream(files).distinct()
                .filter(file -> file.isDirectory())
                .map(File::getName)
                .map(dName-> Util.joinPaths(root, dName))
                .collect(Collectors.toList());
    }

    public List<PackageMetrics> getPackageMetricsList() {
        return packageMetricsList;
    }
}
