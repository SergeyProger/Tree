
package treeview;

import java.io.*;
import java.util.Iterator;

public class BinaryTree<E extends Comparable<E>> {
    private TreeNode<E> root;
    private int size = 0;

    /**
     * Конструктор без параметров
     */
    public BinaryTree() {
    }

    /**
     * Создает BinaryTree из массива объектов любого типа
     */
    public BinaryTree(E[] objects) {
        for (int i = 0; i < objects.length; i++) {
            insert(objects[i]);
        }
    }

    /**
     *  Возвращает true, если элемент был найден в дереве
     */
    public boolean search(E e) {
        TreeNode<E> current = root; // Инициализируем корень

        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                current = current.right;
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * Элемент вставлен
     * Возвращает true, если элемент был успешно вставлен
     */
    public boolean insert(E e) {
        if (root == null)
            root = createNode(e); // Создаем новый корень
        else {
            // Родительский узел
            TreeNode<E> parent = null;
            TreeNode<E> current = root;
            while (current != null)
                if (e.compareTo(current.element) < 0) {
                    parent = current;
                    current = current.left;
                } else if (e.compareTo(current.element) > 0) {
                    parent = current;
                    current = current.right;
                } else {
                    return false; // Дублированные узлы не добавляются
                }
            if (e.compareTo(parent.element) < 0)
                parent.left = createNode(e);
            else
                parent.right = createNode(e);
        }

        size++;
        return true; // Добавлен элемент
    }

    /**
     * Возвращает глубину дерева путем рекурсивного пересечения дерева
     * и глубина int увеличена постепенно
     */
    public int getDepth(TreeNode<E> node) {
        if (node == null) {
            return (0);
        } else {
            // вычисляет глубину каждого поддерева
            int lDepth = getDepth(node.left);
            int rDepth = getDepth(node.right);
           // большее значение возвращается как результат
            if (lDepth > rDepth) {
                return (lDepth + 1);
            }
            else {
                return (rDepth + 1);
            }
        }
    }

    /**
     * Создать новый узел
     */
    protected TreeNode<E> createNode(E e) {
        return new TreeNode<E>(e);
    }

    /**
     * В порядке от корня
     */
    public void inOrder() {
        inOrder(root);
    }

    
    protected void inOrder(TreeNode<E> root) {
        if (root == null) return;
        inOrder(root.left);
        System.out.print(root.element + " ");
        inOrder(root.right);
    }

    
    public void postOrder() {
        postOrder(root);
    }

    protected void postOrder(TreeNode<E> root) {
        if (root == null) return;
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.element + " ");
    }

    public void preOrder() {
        preOrder(root);
    }
    protected void preOrder(TreeNode<E> root) {
        if (root == null) return;
        System.out.print(root.element + " ");
        preOrder(root.left);
        preOrder(root.right);
    }

    /**
     * Внутренний класс TreeNode <E>
     * Содержит левый и правый дочерний узел
     */
    public static class TreeNode<E extends Comparable<E>> {
        E element;
        TreeNode<E> left;
        TreeNode<E> right;
        public TreeNode(E e) {
            element = e;
        }
    }

    /**
     *Количество узлов в дереве
     */
    public int getSize() {
        return size;
    }

    /**
     * Возвращает корень дерева
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * Сохраните дерево в файл (элементы просто записываются в файл один за другим)
     */
    public void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter("output.dat", "UTF-8");
            Iterator<String> iterator = this.iterator();
            while (iterator.hasNext()) {
                writer.println(iterator.next().toString());
            }
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Загружаем дерево из файла
     */
    public void loadFromFile() {
        try {
            FileReader in = new FileReader("output.dat");
            BufferedReader br = new BufferedReader(in);
            clear();
            String elem;
            while ((elem = br.readLine()) != null) {
                insert((E)(Object)elem);
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Удалить элемент из дерева
     * Возвращает true, если элемент был успешно удален
     * Возвращает false, если элемент не найден в дереве
     */
    public boolean delete(E e) {
        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                parent = current;
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                parent = current;
                current = current.right;
            } else
                break; // Элемент был найден в дереве
        }

        if (current == null)
            return false; 
        // Случай 1: текущий не имеет левого дочернего узла
        if (current.left == null) {
            // Узел предварительного кода текущего узла является правильным дочерним узлом
            // подключен к текущему узлу
            if (parent == null) {
                root = current.right;
            } else {
                if (e.compareTo(parent.element) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;
            }
        } else {
            // Случай 2: текущий узел имеет левый дочерний узел
            // Найдите узел, расположенный дальше вправо в левом поддереве
            // текущий узел и его предшественники
            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;

            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right; // повернуть направо
            }

            // Элемент заменяется на тот, который находится дальше всего справа
            current.element = rightMost.element;
            //  Самый дальний узел будет удален
            if (parentOfRightMost.right == rightMost)
                parentOfRightMost.right = rightMost.left;
            else
            //  элемент, который нужно удалить, уже самый правый элемент
                parentOfRightMost.left = rightMost.left;
        }
        size--;
        return true;
    }

    /**
     * Возвращает пользовательский итератор (необходим для хранения)
     */
    public java.util.Iterator iterator() {
        return new PreOrderedIterator();
    }

    /**
     * Встроенный класс, который позволяет выполнять итерацию через дерево
     */
    class PreOrderedIterator implements java.util.Iterator {

        private java.util.ArrayList<E> list = new java.util.ArrayList<E>();
        private int current = 0;

        /**
         *  Конструктор заполняет список, в котором хранятся все элементы дерева
         */
        public PreOrderedIterator() {
            preOrder(); // Проходим по дереву и сохраняет элементы в списке
        }

        /**
         *  Обход вниз от корневого узла
         */
        private void preOrder() {
            preOrder(root);
        }

        /**
         *  Обход вниз от заданного узла
         */
        private void preOrder(TreeNode<E> root) {
            if (root == null) return;
            list.add(root.element);
            preOrder(root.left);
            preOrder(root.right);
        }

        /**
         * Gibt es noch weitere Elemente?
         */
        public boolean hasNext() {
            if (current < list.size())
                return true;

            return false;
        }

        /**
         * Возвращает  следующий элемент
         */
        public Object next() {
            return list.get(current++);
        }

        /**
         * Удаляет текущий элемент и повторно загружает дерево
         */
        public void remove() {
            delete(list.get(current)); // Удаляет текущий элемент
            list.clear(); // Очистить список
            inOrder(); // Перестроить список
        }
    }

    /**
     * Удаляет все элементы из дерева (Сброс)
     */
    public void clear() {
        root = null;
        size = 0;
    }
}