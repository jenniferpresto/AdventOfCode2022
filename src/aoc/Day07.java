package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day07 {
    final String dataFile = ""
    private static class Directory {
        final Long MAX_SIZE = 100000L;
        public final String name;
        public final Directory parent;
        public final List<Directory> subDirectories;
        public final List<ElfFile> elfFiles;

        Directory(String name, Directory parent) {
            this.name = name;
            this.parent = parent;
            subDirectories = new ArrayList<>();
            elfFiles = new ArrayList<>();
        }

        public Directory getSubDirectoryByName(String name) {
            for (Directory directory : this.subDirectories) {
                if(directory.name.equals(name)) {
                    return directory;
                }
            }
            return null;
        }

        public void addDirectory(Directory dir) {
            subDirectories.add(dir);
        }

        public void addElfFile(ElfFile f) {
            elfFiles.add(f);
        }

        public Long getSize() {
            Long size = 0L;
            for (ElfFile file : elfFiles) {
                size += file.size;
            }
            for (Directory dir : subDirectories) {
                size += dir.getSize();
            }
            return size;
        }

        public Long getSizeBelowMax() {
            Long totalSubdirSize = 0L;
            for (Directory dir : subDirectories) {
                totalSubdirSize += dir.getSizeBelowMax();
            }
            Long ownSize = getSize();
            if (ownSize <= MAX_SIZE) {
                return totalSubdirSize + ownSize;
            } else {
                return totalSubdirSize;
            }
        }
    }

    private static class ElfFile {
        public final String name;
        public final Long size;
        ElfFile(String name, Long size) {
            this.name = name;
            this.size = size;
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("testData/Day07.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        }
        Directory root = createFilesystem(data);
        System.out.println("Total size of root: " + root.getSize());
        System.out.println("Total size of max dirs: " + root.getSizeBelowMax());

    }

    private static Directory createFilesystem(List<String> data) {
        Directory rootDirectory = new Directory("/", null);
        Directory currentDirectory = rootDirectory;

        //  create the filesystem
        for (String line : data) {
            String[] termOutput = line.split(" " );

            //  instructions
            if (termOutput[0].startsWith("$")) {
                if (termOutput[1].equals("cd")) {
                    if (termOutput[2].equals("..")) {
                        currentDirectory = currentDirectory.parent;
                    } else if (termOutput[2].equals("/")) {
                        currentDirectory = rootDirectory;
                    } else {
                        currentDirectory = currentDirectory.getSubDirectoryByName(termOutput[2]);
                    }
                } else if (termOutput[1].equals("ls")){
                    // System.out.println("ls: going to the next instruction");
                }
            }
            //  directories
            else if (termOutput[0].startsWith("dir")) {
                if (currentDirectory.getSubDirectoryByName(termOutput[1]) == null) {
                    Directory newSub = new Directory(termOutput[1], currentDirectory);
                    currentDirectory.addDirectory(newSub);
                } else {
                    System.out.println("This is a repeat");
                }
            }
            //  files
            else {
                ElfFile elfFile = new ElfFile(termOutput[1], Long.valueOf(termOutput[0]));
                currentDirectory.addElfFile(elfFile);
            }
        }
        return rootDirectory;
    }
}
