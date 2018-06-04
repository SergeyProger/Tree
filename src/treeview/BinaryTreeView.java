package treeview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BinaryTreeView extends JPanel {
    private BinaryTree<String> tree; // BinaryTree, що відображається або заповнюється
    private JTextField jtfKey = new JTextField(5);
    private PaintTree paintTree = new PaintTree();

    //Buttons
    private JButton jbtInsert = new JButton("Вставить");
    private JButton jbtDelete = new JButton("Удалить");
    private JButton jbtSave = new JButton("Сохранить");
    private JButton jbtLoad= new JButton("Загрузить");
    private JButton jbtReset = new JButton("Сброс");


    private JLabel jlbDepth = new JLabel("0");
    private JLabel jlbSize = new JLabel("0");

    public BinaryTreeView(BinaryTree<String> tree) {
        this.tree = tree; 
        setUI();
    }

    /* Инициализирует пользовательский интерфейс для двоичного дерева */
    private void setUI() {
        this.setLayout(new BorderLayout());
        add(paintTree, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Узлы: "));
        panel.add(jlbSize);
        panel.add(new JLabel("  Глубина: "));
        panel.add(jlbDepth);
        panel.add(new JLabel("        Значение: "));
        panel.add(jtfKey);
        panel.add(jbtInsert);
        panel.add(jbtDelete);
        panel.add(new JLabel("    "));
        panel.add(jbtSave);
        panel.add(jbtLoad);
        panel.add(new JLabel("    "));
        panel.add(jbtReset);
        add(panel, BorderLayout.SOUTH);

        //// слушиватель событий для кнопок
        jbtInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String key = jtfKey.getText();
                if (tree.search(key)) { // Ключ был найден в дереве
                    JOptionPane.showMessageDialog(null, key + " уже существует");
                }
                else {
                    if (key.length() <= 3 && key.length() > 0) {
                        tree.insert(key);
                        paintTree.repaint();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Значение слишком длинное или пустое");
                    }
                }
            }
        });

        jbtDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String key = jtfKey.getText();
                if (!tree.search(key)) { // ключа нет в дереве
                    JOptionPane.showMessageDialog(null,
                            key + " Недоступно");
                }
                else {
                    tree.delete(key); //Удаляем ключь
                    paintTree.repaint(); // Повторно отобразить дерево
                }
            }
        });

        jbtSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tree.saveToFile();
                JOptionPane.showMessageDialog(null,"Дерево сохранено.");
            }
        });

        jbtLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tree.loadFromFile();
                paintTree.repaint(); 
                JOptionPane.showMessageDialog(null,"Дерево загружено.");
            }
        });

        jbtReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tree.clear();
                paintTree.repaint(); 
            }
        });


    }

    /* Вложенный класс PaintTree, который рисует представление узлов и соединительных линий*/
    class PaintTree extends JPanel {
        private int radius = 20; // Радиус узла
        private int vGap = 50; //вертикальное расстояние между уровнями

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (tree.getRoot() != null) {
                // Дерево построено рекурсивно
                displayTree(g, tree.getRoot(), getWidth() / 2, 30, getWidth() / 4);
            }
            jlbDepth.setText(String.valueOf(tree.getDepth(tree.getRoot())));
            jlbSize.setText(String.valueOf(tree.getSize()));
        }

        
         /** Рисует дерево вниз из поддерева  */
        private void displayTree(Graphics g, BinaryTree.TreeNode root, int x, int y, int hGap) {
           // Представляет корень
            g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.drawString(root.element + "", x - 6, y + 4);

            if (root.left != null) {
               // Нарисуем линию в левый узел
                connectLeftChild(g, x - hGap, y + vGap, x, y);
                
               // Левое поддерево
                displayTree(g, root.left, x - hGap, y + vGap, hGap / 2);
            }

            if (root.right != null) {
                // Нарисуем линию на правый узел
                connectRightChild(g, x + hGap, y + vGap, x, y); 
               // Правое поддерево  
                displayTree(g, root.right, x + hGap, y + vGap, hGap / 2);
            }
        }

        
          /** Присоединить узел (x2, y2) с помощью
           * его левый дочерний узел (x1, y1) */
        private void connectLeftChild(Graphics g, int x1, int y1, int x2, int y2) {
            double d = Math.sqrt(vGap * vGap + (x2 - x1) * (x2 - x1));
            int x11 = (int)(x1 + radius * (x2 - x1) / d);
            int y11 = (int)(y1 - radius * vGap / d);
            int x21 = (int)(x2 - radius * (x2 - x1) / d);
            int y21 = (int)(y2 + radius * vGap / d);
            g.drawLine(x11, y11, x21, y21);
        }

        
         /** Присоединить узел (x2, y2) с помощью
         * его правый дочерний узел (x1, y1) */
        private void connectRightChild(Graphics g, int x1, int y1, int x2, int y2) {
            double d = Math.sqrt(vGap * vGap + (x2 - x1) * (x2 - x1));
            int x11 = (int)(x1 - radius * (x1 - x2) / d);
            int y11 = (int)(y1 - radius * vGap / d);
            int x21 = (int)(x2 + radius * (x1 - x2) / d);
            int y21 = (int)(y2 + radius * vGap / d);
            g.drawLine(x11, y11, x21, y21);
        }
    }

        /** Основной метод запускает все
        * Здесь также может быть передан существующий BinaryTree */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Алгоритм  дерева решений");
        JPanel applet = new BinaryTreeView(new BinaryTree<String>()); 
        frame.add(applet);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}