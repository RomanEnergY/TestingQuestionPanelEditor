package QuestionEditor.ClassPerformers;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by user on 09.10.2018.
 */
public class TreePathIcon {
    // TreePathAnswer{
    //    treePathsCorrect=[]
    //    treePathsWrong=[[Программа, Диспетчер РЭС, Раздел 1 - Правила технической эксплуатации, Инструкция по переключениям в электроустановках, приказ МЭ РФ № 266 от 30.06.03 г., 10359], [Программа, Диспетчер РЭС, Раздел 1 - Правила технической эксплуатации, Инструкция по переключениям в электроустановках, приказ МЭ РФ № 266 от 30.06.03 г., 10347]]}

    private ParsingConstant parsing;
    private Icon iconCorrect;
    private Icon iconWrong;
    private HashSet<TreePath> treePathsCorrect;
    private HashSet<TreePath> treePathsWrong;
    private final int key = 43;

    public TreePathIcon(Icon iconCorrect, Icon iconWrong) {
        this.parsing = new ParsingConstant();
        this.iconCorrect = iconCorrect;
        this.iconWrong = iconWrong;
        this.treePathsCorrect = new HashSet<>();
        this.treePathsWrong = new HashSet<>();

    }


    @Override
    public String toString() {
        return "TreePathAnswer{" +
                "iconCorrect=" + iconCorrect +
                ", treePathsCorrect=" + Arrays.toString(treePathsCorrect.toArray()) +
                ", iconWrong=" + iconWrong +
                ", treePathsWrong=" + Arrays.toString(treePathsWrong.toArray()) +
                '}';
    }

    public String toStringList() {
        return "TreePathAnswer{" +
                "\ntreePathsCorrect=" + Arrays.toString(treePathsCorrect.toArray()) +
                "\ntreePathsWrong=" + Arrays.toString(treePathsWrong.toArray()) +
                '}';
    }

    public void add(TreePath treePath, boolean b) {
        if (b) {
            this.treePathsCorrect.add(treePath);
            this.treePathsWrong.remove(treePath);
        } else {
            this.treePathsWrong.add(treePath);
            this.treePathsCorrect.remove(treePath);
        }
    }

    public void remove(TreePath treePath, boolean b) {
        if (b) {
            this.treePathsCorrect.remove(treePath);
        } else {
            this.treePathsWrong.remove(treePath);
        }
    }

    public boolean isInStock(TreePath pathForRow) {
        for (TreePath treePath : this.treePathsCorrect) {
            if (treePath.equals(pathForRow)) {
                return true;
            }
        }

        for (TreePath treePath : this.treePathsWrong) {
            if (treePath.equals(pathForRow)) {
                return true;
            }
        }

        return false;
    }

    public Icon getIcon(TreePath pathForRow) {
        for (TreePath treePath : this.treePathsCorrect) {
            if (treePath.equals(pathForRow)) {
                return this.iconCorrect;
            }
        }

        for (TreePath treePath : this.treePathsWrong) {
            if (treePath.equals(pathForRow)) {
                return this.iconWrong;
            }
        }

        return null;
    }

    public int getCountCorrect() {
        return this.treePathsCorrect.size();
    }

    public int getCountWrong() {
        return this.treePathsWrong.size();
    }

    private void writerFileTreePathIcon() {
        try {
            int checkSum = 0;
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder stringWrite = new StringBuilder();

            FileWriter writer = new FileWriter("setter.asop", false);
            for (TreePath treePath : this.treePathsWrong) {
                stringBuilder.append(this.parsing.tegLength[0]).append(treePath.getPath().length).append(this.parsing.tegLength[1]);
                for (Object o : treePath.getPath()) {
                    stringBuilder.append(this.parsing.tegTreePath[0]).append(o.toString()).append(this.parsing.tegTreePath[1]);
                }
                // контрольная сумма
                for (byte b : stringBuilder.append(this.parsing.tegBoolean[0]).append(false).append(this.parsing.tegBoolean[1]).toString().getBytes())
                    checkSum = checkSum + b;

                String s = stringBuilder.append("<C").append(checkSum).append("/c>").toString();
                for (int i = 0; i < s.length(); i++) {
                    stringWrite.append((char) (s.charAt(i) + key));
                }


                writer.write(stringWrite.append("\r\n").toString());
                stringBuilder = new StringBuilder();
                stringWrite = new StringBuilder();
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void readerFileTreePathIcon() {
        try {
            File file = new File("setter.asop");
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            // читаем посимвольно
            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    stringBuilder.append((char) (line.charAt(i) - key));
                }
                // TODO организовать чтение из файла и занесение данных в объект
                int lengthTreePath = this.parsing.getLengthTreePath(stringBuilder.toString());
                TreePath treePath = this.parsing.getTreePath(stringBuilder.toString(), lengthTreePath);
                boolean b = this.parsing.getBool(stringBuilder.toString());

                this.add(treePath, b);

                stringBuilder = new StringBuilder();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void exit() {
        this.writerFileTreePathIcon();
    }


    private class ParsingConstant {
        final String[] tegLength = new String[]{"<L", "/l>"};
        final String[] tegTreePath = new String[]{"<D", "/d>"};
        final String[] tegBoolean = new String[]{"<I", "/i>"};

        private int getLengthTreePath(String text) {
            int offset = this.tegLength[0].length() + text.indexOf(this.tegLength[0], 0);
            int count = text.indexOf(this.tegLength[1], 0);

            return Integer.parseInt(String.copyValueOf(text.toCharArray(), offset, count - offset));
        }

        private TreePath getTreePath(String text, int lengthTreePath) {
            ArrayList<Object> objects = new ArrayList<>();
            int offset = 0;
            int count = 0;
            while (text.indexOf(this.tegTreePath[0], count) != -1) {
                offset = this.tegTreePath[0].length() + text.indexOf(this.tegTreePath[0], count);
                count = text.indexOf(this.tegTreePath[1], offset);
                objects.add(String.copyValueOf(text.toCharArray(), offset, count - offset));
            }
            if (objects.size() == lengthTreePath)
                return new TreePath(objects.toArray());
            else
                return null;
        }

        private boolean getBool(String text) {
            int offset = this.tegBoolean[0].length() + text.indexOf(this.tegBoolean[0]);
            int count = text.indexOf(this.tegBoolean[1], offset);
            return Boolean.getBoolean(String.copyValueOf(text.toCharArray(), offset, count - offset));
        }
    }
}
