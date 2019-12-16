public class Main {
    public static void main(String[] args) {
        // Создаем дерево и наполянем его элементами
        AvlTree<Integer> avl = new AvlTree<>();
        avl.add(1);
        avl.add(2);
        avl.add(3);
        avl.add(4);
        avl.add(9);
        avl.add(10);
        avl.add(11);
        avl.add(12);
        avl.add(13);
        avl.add(14);
        avl.add(15);
        avl.add(5);
        avl.add(6);
        avl.add(7);
        avl.add(8);
        avl.add(16);
        avl.add(17);
        // Печатаем дерево на консоль
        System.out.println(avl.toString());
    }
}
