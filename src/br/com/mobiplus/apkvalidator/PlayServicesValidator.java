package br.com.mobiplus.apkvalidator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PlayServicesValidator {
    String apkToolJar;
    String apksDir;

    public void setApkToolJar(String apkToolJar) {
        this.apkToolJar = apkToolJar;
    }

    public void setApksDir(String apksDir) {
        this.apksDir = apksDir;
    }

    private List<IntegerRes> readPlayServicesVersion(File integersFilePath) {
        XStream stream = new XStream(new DomDriver());
        stream.alias("resources", List.class);
        stream.registerConverter(new IntegerConverter());
        stream.alias("integer", IntegerRes.class);
        return (ArrayList<IntegerRes>) stream.fromXML(integersFilePath);
    }

    private String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            //p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public void run() {
        List<File> listApkFiles = this.getApkFilesFrom(apksDir);

        for (File apkFile : listApkFiles) {
            String commandResult = this.decompile(apkFile);

            File integersFilePath = this.getIntegersFilePath(apkFile);

            List<IntegerRes> integerResList = this.readPlayServicesVersion(integersFilePath);

            for (IntegerRes integerRes : integerResList) {

                if ("google_play_services_version".equals(integerRes.getName())) {
                    System.out.println("Show result for file: " + apkFile.getName());
                    System.out.println(integerRes.getName() + ":" + integerRes.getValue());
                    break;
                }
            }


            //System.out.println(commandResult);
        }
    }

    private File getIntegersFilePath(File apkFile) {
        String fileName = FilenameUtils.getBaseName(apkFile.getAbsolutePath());
        File file = new File(fileName + "/res/values/integers.xml");
        if (file.exists()) {
            return file;
        }
        throw new RuntimeException("Integers file not exists for '" + fileName + "'. Verify inside AndroidManifest.xml ");
    }

    private String decompile(File apkFile) {
        String command = String.format("java -jar %s d -f %s", apkToolJar, apkFile.getAbsolutePath());
        System.out.println(String.format("\nExecuting command: [%s]", command));
        return this.executeCommand(command);
    }

    private List<File> getApkFilesFrom(String apksDir) {
        File fileApksDir = new File(apksDir);

        if (!fileApksDir.isDirectory()) {
            throw new RuntimeException(String.format("apksDir '%s' is not a directory!", apksDir));
        }

        File[] files = fileApksDir.listFiles();
        List<File> listFiles = new ArrayList<File>(files.length);

        for (int i = 0; i < files.length; i++) {
            String extension = FilenameUtils.getExtension(files[i].getName());

            if (this.isApkFile(extension)) {
                System.out.println(String.format("Using APK file named '%s'", files[i].getAbsolutePath()));
                listFiles.add(files[i]);
            } else {
                System.out.println(">>>> Discarding non APK file. " + files[i].getAbsolutePath());
            }
        }
        return listFiles;
    }

    private boolean isApkFile(String extension) {
        return "apk".equals(extension);
    }
}