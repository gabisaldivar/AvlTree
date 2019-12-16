import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

    // АВЛ-дерево
    public class AvlTree<T extends Comparable<T>> implements Set<T> {
        // Корневой узел дерева
        private Node<T> root;
        // Variable that stores the size of the tree
        private int size = 0;

        // Метод, который возвразает размер дерева
        @Override
        public int size() {
            return size;
        }

        // Метод который указывает на то, пустое ли дерево
        @Override
        public boolean isEmpty() {
            // Дерево пустое, если у него нет коренового элемента
            return root == null;
        }

        // Метод, который проверяет есть ли передаваемый элемент в дереве
        @Override
        public boolean contains(Object value) {
            return contains((T) value, root);
        }

        // Метод возвращающий итератор дерева
        @Override
        public Iterator<T> iterator() {
            return new AvlIterator<>(root);
        }

        @Override
        public Object[] toArray() {
           return null;
        }

        @Override
        public <E> E[] toArray(E[] array) {
            return null;
        }

        // Метод для добавления элемента в массив
        @Override
        public boolean add(T value) {
            if (!contains(value)) {
                root = insert(value, root);
                size++;
                return true;
            } else {
                return false;
            }
        }

        // Метод для удаления элемента дерева
        @Override
        public boolean remove(Object value) {
            // Если такой элемент есть в дереве - удаляем его
            if (contains(value)) {
                root = delete((T) value, root);
                // уменьшаем размер дерева
                size--;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
           return false;
        }

        @Override
        public boolean addAll(Collection<? extends T> collection) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
           return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
           return false;
        }

        // Метод для очистки дерева
        @Override
        public void clear() {
            size = 0;
            root = null;
        }

        // Метод для сравнения двух элементов
        private int compare(T t1, T t2) {
            return t1.compareTo(t2);
        }

        // Рекурсивный метод для добавления нового элемента в дерево
        private Node<T> insert(T value, Node<T> node) {
            // Если текущий узел равен нал - добавляем новое занчение
            if (node == null) {
                return new Node<>(value, null, null);
            }
            // Основываясь на значениях идем по нужной ветке, пока не найдем нужное место
            int compare = compare(value, node.value);
            if (compare != 0) {
                if (compare < 0) {
                    node.left = insert(value, node.left);
                } else {
                    node.right = insert(value, node.right);
                }
                // После добавления балансируем дерево на всех пройденных узлах
                return balance(node);
            }
            return null;
        }

        // Рекурсивный метод поиска элемента
        private boolean contains(T value, Node<T> node) {
            while (node != null) {
                int compare = compare(value, node.value);
                if (compare < 0) {
                    node = node.left;
                } else if (compare > 0) {
                    node = node.right;
                } else {
                    return true;
                }
            }
            return false;
        }

        // рекурсивный метод для удаления узла из дерева
        private Node<T> delete(T value, Node<T> node) {
            if (node != null) {
                // Рекурсивно идем по дереву основываясь на значении тукщего и удаляемого элементов
                int compare = compare(value, node.value);
                if (compare < 0) {
                    node.left = delete(value, node.left);
                } else if (compare > 0) {
                    node.right = delete(value, node.right);
                } else if (node.left != null && node.right != null) {
                    node.value = findMin(node.right).value;
                    node.right = delete(node.value, node.right);
                } else {
                    node = (node.left != null) ? node.left : node.right;
                }
                // Балансируем дерево
                return balance(node);
            }
            return null;
        }

        // Метод, который проверяет высоты поддеревьев и при необходимости выполняет нужные повороты
        private Node<T> balance(Node<T> node) {
            if (node != null) {
                if (height(node.left) - height(node.right) > 1) {
                    if (height(node.left.left) >= height(node.left.right)) {
                        node = smallLeftRotation(node);
                    } else {
                        node = bigLeftRotation(node);
                    }
                } else if (height(node.right) - height(node.left) > 1) {
                    if (height(node.right.right) >= height(node.right.left)) {
                        node = smallRightRotation(node);
                    } else {
                        node = bigRightRotation(node);
                    }
                }
                node.height = Math.max(height(node.left), height(node.right)) + 1;
            }
            return node;
        }

        // Метод для поиска узла с минимальным значением
        private Node<T> findMin(Node<T> node) {
            // Идем к самому концу левой ветки, где и будет находиться минимальный элемент
            if (node != null) {
                while (node.left != null) {
                    node = node.left;
                }
            }
            return node;
        }

        // Рекурсивный метод для формирвоания строки
        private StringBuilder toString(Node<T> node, StringBuilder prefix, boolean isTail, StringBuilder sb) {
            // Добавляем в строку правое поддерево
            if (node.right != null) {
                toString(node.right, new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
            }
            // Добавляем в строку текущий элемент
            sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.value.toString()).append("\n");
            // Добавляем в строку левое поддерево
            if (node.left != null) {
                toString(node.left, new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
            }
            return sb;
        }

        // Метод для визцализации
        @Override
        public String toString() {
            return toString(root, new StringBuilder(), true, new StringBuilder()).toString();
        }

        // Метод для вычисления высоты поддерева
        private int height(Node<T> node) {
            return node == null
                    ? -1
                    : node.height;
        }

        // Малый левый поворот

        private Node<T> smallLeftRotation(Node<T> node) {
            Node<T> left = node.left;
            node.left = left.right;
            left.right = node;
            node.height = Math.max(height(node.left), height(node.right)) + 1;
            left.height = Math.max(height(left.left), node.height) + 1;
            return left;
        }

        // Малый правый поврот
        private Node<T> smallRightRotation(Node<T> node) {
            Node<T> right = node.right;
            node.right = right.left;
            right.left = node;
            node.height = Math.max(height(node.left), height(node.right)) + 1;
            right.height = Math.max(height(right.right), node.height) + 1;
            return right;
        }

        // Большой левый поворот
        private Node<T> bigLeftRotation(Node<T> node) {
            node.left = smallRightRotation(node.left);
            return smallLeftRotation(node);
        }

        // Большой правый поворот
        private Node<T> bigRightRotation(Node<T> node) {
            node.right = smallLeftRotation(node.right);
            return smallRightRotation(node);
        }

        // Класс-итератор
        private class AvlIterator<E> implements Iterator<E> {
            private Stack<Node<E>> stack;

            // Создаем стек и добавляем в него всю левую ветку
            public AvlIterator(Node<E> root) {
                stack = new Stack<>();
                Node<E> localNode = root;

                while (localNode != null) {
                    stack.push(localNode);
                    localNode = localNode.left;
                }
            }

            // Если стек не пустой - проход по дереву еще не закончен
            @Override
            public boolean hasNext() {
                return !stack.empty();
            }

            // Метод для возвращения следующего элемента
            @Override
            public E next() {
                // Аолучаем верхний элемент сетка
                Node<E> node = stack.pop();
                E e = node.value;
                // Если у него есть правая ветка - добавляем все ее левые подветки в стек
                if (node.right != null) {
                    node = node.right;
                    while (node != null) {
                        stack.push(node);
                        node = node.left;
                    }
                }
                return e;
            }
        }

        // Класс описывающий узел дерева
        public class Node<E> {

            // Поле для хранения значения
            private E value;
            // Высота текущего поддерева
            private int height;
            // Левая ветка
            private Node<E> left;
            // Правая ветка
            private Node<E> right;

            // Конструктор для создания нового узла
            Node(E value, Node<E> left, Node<E> right) {
                this.value = value;
                this.left = left;
                this.right = right;
                this.height = 0;
            }

        }
    }
