import java.util.Scanner;

//Лабораторная работа №4
//Стеки
//Плотников Владислав, 951005


public class Main {
    //Метод для "чтения записи справа налево"
    private static String getReversed(String s){
        String buf = "";
        //Накапливаем переворот
        for (int i = s.length() - 1; i >= 0; i--){
            if (s.charAt(i) == ')') buf += '(';
                else
            if (s.charAt(i) == '(') buf += ')';
                else                buf += s.charAt(i);
        }
        //Возвращаем перевертыш
        return buf;
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        //Объектная переменная для образования обратной польской записи
        INtoRPN toPolish;

        System.out.println("Введите корректное инфиксное выражение: ");

        try {
            //Читаем выражение
            String buf = in.nextLine();
            //Делаем обработку
            toPolish = new INtoRPN(buf);
            String RPN = toPolish.getReversedPolishNotation();
            //Выводим польскую запись
            System.out.println("Обратная польская запись: " + RPN);
            //Отправляем на обработку перевернутую исходную строку
            toPolish = new INtoRPN(getReversed(buf));
            //Переворачиваем справа налево результат и получаем префиксную запись
            System.out.println("Префиксная запись: " + getReversed(toPolish.getReversedPolishNotation()));

            //Обработка ошибока ввода
        } catch (RuntimeException e){
            String msg = e.getMessage();

            switch (msg) {
                case "SHORT" -> System.out.println("Слишком короткое инфиксное выражение!");
                case "WH_SPACE" -> System.out.println("Инфиксное выражение содержит пробелы!");
                case "NUM" -> System.out.println("Инфиксное выражение содержит числа!");
                case "OPERAND_WR" -> System.out.println("В инфиксном выражении некорректно записан операнд!");
                case "OPERATOR_WR" -> System.out.println("В инфиксном выражении присутствуют посторонние символы!");
                default -> System.out.println("В инфиксном выражении не соблюден баланс скобок!");
            }
        }
        //Закрываем чтение
        in.close();
    }
}
