import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Класс INfix to Reversed-Polish-Notation
class INtoRPN {
    //Строка для хранения изначального выражения в ИНФИКСНОЙ форме
    private String infix;
    //Строка для хранения результирующей ПОСТФИКСНОЙ форме
    private String rPostfix;
    //Стек для выполнения преобразований
    private Stack stack;
    //Конструктор класса
    public INtoRPN(String s) throws  RuntimeException{
        //Проверяем корректность введеного инфиксного выражения
        checkCorrectness(s);
        //Все в порядке? Запоминаем инфиксное выражение
        infix = s;
        //Пока результирующую строку делаем пустой
        rPostfix = "";
        //Начинаем преобразование
        makeRPN();
    }
    //Метод для преобразования в ПОСТФИКСНУЮ форму
    private void makeRPN(){
        //Создаем пустой стек
        stack = new Stack();
        //Посимвольно проходимся по строке - инфиксному выражению
        for(int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);

            switch(ch)
            {
                case '+':
                case '-':
                    //Плюс или минус? Обрабатываем с приоритетом 2
                    processOperation(ch, 2);
                    break;
                case '*':
                case '/':
                    //Умножение или деление? Обрабатываем с приоритетом 3
                    processOperation(ch, 3);
                    break;
                case '^':
                    //Возведение в степень? Обрабатываем с приоритетом 4
                    processOperation(ch, 4);
                    break;
                case '(':
                    //Открывающая скобка? Просто кладём её в стек с приоритетом 0
                    stack.push(new Node('(', 0));
                    break;
                case ')':
                    //Закрывающая скобка? Обрабатываем с приоритетом 1
                    processBracket(ch, 1);
                    break;
                default:
                    //Операнд? Сразу присоединяем к ответу
                    rPostfix = rPostfix + ch;
                    break;
            }
        }
        //Дописываем то, что осталось в стеке операций
        while(!stack.isEmpty() )
            rPostfix = rPostfix + stack.pop().getOperation();

    }
    //Метод для обработки бинарного оператора
    private void processOperation(char op, int prior){
        //Покуда стек не пуст
        while(!stack.isEmpty() ) {
            //Очищаем вершину стека. Смотрим, какая операция там была
            char opTop = stack.pop().getOperation();
            if(opTop == '(') {
                //Была открывающаяся скобка? Кладём её обратно
                stack.push(new Node(opTop, 0));
                break;
            }
            else {
                //Переменая для приоритета операции, находившейся в верхушке стека
                int prior2;
                //Плюс или минус? Приоритет 2.
                if (opTop == '+' || opTop == '-')
                    prior2 = 2;
                //Умножить или делить? Приоритет 3
                else if (opTop == '*' || opTop == '/')
                    prior2 = 3;
                //Возведение в степень? Приоритет 4
                else
                    prior2 = 4;
                //Приоритет нового оператора меньше старого?
                if(prior2 < prior) {
                    //Кладем в стек новый оператор
                    stack.push(new Node(opTop, prior2));
                    break;
                }
                else
                    //Сразу присоединяем оператор к ответу
                    rPostfix = rPostfix + opTop;
            }
        }
        //Кладем оператор в стек операторов
        stack.push(new Node(op, prior));
    }
    //Метод для обработки закрывающейся скобки
    private void processBracket(char op, int prior){
        //Покуда стек не пуст
        while( !stack.isEmpty() ) {
            //Извлекаем текующий вершинку стека (символ)
            char chx = stack.pop().getOperation();
            if(chx == '(')
            //Прекращаем выполнение
                break;
            else
            //Приписываем к ответу
                rPostfix = rPostfix + chx;
        }
    }
    //Возвращаем полученную обратную польскую нотацию
    public String getReversedPolishNotation(){
        return rPostfix;
    }
    //Метод для проверки поступаемого инфиксного выражения
    private void checkCorrectness(String s){
        if (s.length() < 3) throw new RuntimeException("SHORT");        //Короткая строка? Бьем тревогу
        if (s.contains(" ")) throw new RuntimeException("WH_SPACE");    //Есть пробелы? Бьем тревогу

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        if (m.find()) throw new RuntimeException("NUM");                //Есть цифры? Бьем тревогу

        char[] arr = s.toCharArray();                                   //Переводим в массив char[]
        final String operands = "+-*/^";                                //Строка с разрешимыми операторами
        Stack brStack = new Stack();                                    //Создаем пустой стек для проверки скобочек

        for (int i = 0; i < arr.length - 1; i++) {
            if (Character.isLetter(arr[i]) &&                           //Подряд стоят два операнда
                    Character.isLetter(arr[i + 1]))                     //без знака?
                throw new RuntimeException("OPERAND_WR");               //Бьем тревогу

            if (!Character.isLetter(arr[i]) &&                          //Совсем посторонний символ?
                !Character.isDigit(arr[i]) &&
                arr[i] != '(' &&
                arr[i] != ')' &&
                arr[i] != '+' &&
                arr[i] != '-' &&
                arr[i] != '*' &&
                arr[i] != '/' &&
                arr[i] != '^') throw new RuntimeException("OPERATOR_WR");   //Бьем тревогу

            if (operands.contains(Character.toString(arr[i])) &&            //Несколько подряд идущих операторов? Бьем тревогу
                operands.contains(Character.toString(arr[i + 1])))    throw new RuntimeException("OPERATOR_WR");
        }

        //Проверяем выражение на скобочки
        for (int i = 0; i < arr.length; i++){
            if (arr[i] == '(') brStack.push(new Node(arr[i], 0));
                else
            if (arr[i] == ')' && brStack.peek() == '(') brStack.pop();
        }
        //Нарушен баланс скобок? Бьем тревогу
        if (!brStack.isEmpty()) throw new RuntimeException("BRACKET_WR");
    }
}
