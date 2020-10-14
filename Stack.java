//Класс-стек
class Stack {
    //Ссылка на вершину стека
    private Node top;
    //Конструктор для создания пустого стека
    public Stack(){
        top = null;
    }
    //Пустой ли стек?
    public boolean isEmpty() { return top == null; }
    //Вставляем элемент в стек
    public void push(Node n){
        n.next = top;
        top = n;
    }
    //Очищаем вершину стека
    public Node pop(){
        Node toReturn = top;
        top = top.next;
        return toReturn;
    }
    //Смотрим вершину стека, но не удаляем её
    public char peek(){
        if (top == null) return '~';
        return top.getOperation();
    }
}
