//Класс-узел для хранения пары: операция-приоритет
class Node {
    //Операция
    private final char operation;
    //Приоритет
    private final int priority;
    //Ссылка на следующий элемент стека
    public Node next;
    //Конструктор для инициализации
    public Node(char op, int prior){
        operation = op;
        priority = prior;
        next = null;
    }
    //Геттер
    public char getOperation(){
        return operation;
    }
}
