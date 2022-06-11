package com.adelgrishin;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Intro();
        Program();
    }

    static void Intro() {
        System.out.println();
        System.out.println("                          КУРСОВАЯ РАБОТА");
        System.out.println("  по дисциплине \"Структуры и алгоритмы компьютерной обработки данных\"");
        System.out.println("               на тему \"Исследование алгоритмов поиска\"");
        System.out.println("                   Выполнил: Гришин Адель Ревалевич");
        System.out.println("                         Добро пожаловать!");
        System.out.println();
    }

    static void Program() {
        int[] massiv = new int[]{0};
        int element = 0;

        String string = "";
        String string_search = "";

        int Number_method = Search_Method();

        if (Number_method < 4) {
            massiv = Input_Elements();
            element = Search_Element();
        } else {
            string = Input_String();
            string_search = Search_String();
        }

        switch (Number_method) {
            case 1:
                Linear_Search(massiv, element);
                break;
            case 2:
                Linear_Search_With_Barrier(massiv, element);
                break;
            case 3:
                Binary_Search(massiv, element);
                break;
            case 4:
                Direct_Search(string, string_search);
                break;
            case 5:
                KMP_Search(string, string_search);
                break;
            case 6:
                BM_Search(string.toCharArray(), string_search.toCharArray());
                break;
            default:
                break;
        }

    }

    static int[] Input_Elements() {
        System.out.println("Сколько элементов будет содержать массив? ");
        int n = in.nextInt();
        System.out.println();
        int[] massiv = new int[n];
        System.out.println("Введитe числа: ");
        for (int i = 0; i < n; i++) {
            System.out.print(i+1 + ". ");
            massiv[i] = in.nextInt();
        }
        System.out.println();
        return massiv;
    }

    static String Input_String() {
        System.out.println("Введите строку по которой будет осуществлятсья поиск: ");
        return in.next();
    }

    static int Search_Element() {
        System.out.println("Введите элемент, который будет искаться: ");
        return in.nextInt();
    }

    static String Search_String() {
        System.out.println("Введите искомую подстроку: ");
        return in.next();
    }

    static int Search_Method() {
        System.out.println("_______________________________________________________________________");
        System.out.println("|                      Выберите способ поиска                         |");
        System.out.println("|_____________________________________________________________________|");
        System.out.println("| 1. Линейный поиск                               |    Поиск числа    |");
        System.out.println("| 2. Линейный поиск с барьером                    |     в массиве     |");
        System.out.println("| 3. Двоичный поиск (бинарный)                    |___________________|");
        System.out.println("| 4. Прямой поиск                                 |  Поиск подстроки  |");
        System.out.println("| 5. Алгоритм Кнута, Мориса и Пратта (КМП поиск)  |     в строке      |");
        System.out.println("| 6. Алгоритм Боуера и Мура (БМ поиск)            |                   |");
        System.out.println("|_____________________________________________________________________|");
        System.out.println();
        return in.nextInt();
    }

    static void Linear_Search(int[] massiv, int element) {
        long StartTime = System.nanoTime();
        int i = 0;
        while (i<massiv.length && massiv[i]!=element) i++;
        if (i==massiv.length) Found(-1, StartTime);
        else Found(i, StartTime);
    }

    static void Linear_Search_With_Barrier(int[] massiv, int element) {
        long StartTime = System.nanoTime();
        int n = massiv.length;
        int[] Massiv_X = new int[n+1];
                for (int i = 0; i < n; i++) {
            Massiv_X[i] = massiv[i];
        } if(Massiv_X[n-1] !=element){
            Massiv_X[n]=element;
            for(int i=0;i<n-1;i++){
                while(Massiv_X[i]!=element){
                    i++;
                } Found(i, StartTime);
            }
        } else Found(n-1, StartTime);
    }

    static void Binary_Search(int[] massiv, int element) {
        long StartTime = System.nanoTime();
        int m;
        Arrays.sort(massiv);
        int L = 0;
        int R = massiv.length-1;

        m = (L + R) / 2; // находим средний индекс

        while ((massiv[m] != element) && (L <= R)) {

            if (massiv[m] > element) {  // если число заданного для поиска
                R = m - 1; // уменьшаем позицию на 1.
            } else {
                L = m + 1;    // иначе увеличиваем на 1
            }
            m = (L + R) / 2;
        }
        if (L <= R)  Found(m, StartTime);
        else Found(-1, StartTime);
    }

    static void Direct_Search(String string, String search_string) {
        long StartTime = System.nanoTime();
        int m = search_string.length();
        int n = string.length();
        int id = -1;
        for (int i = 0; i < string.length() - m; i++) {
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (string.charAt(i+j)!= search_string.charAt(j)) flag=false;
            } if (flag) {
                id = i;
                break;
            }
        } Found(id, StartTime);
    }

    static void KMP_Search(String string, String search_string) {
        long StartTime = System.nanoTime();
        int m = search_string.length(); // Длина искомой строки
        int len = 0;
        int k = 1;
        int[] P = new int[m];
        P[0] = 0;

        while (k < m) {
            if (search_string.charAt(k) == search_string.charAt(len)) {
                len++;
                P[k] = len;
                k++;
            } else {
                if (len != 0) {
                    len = P[len - 1];
                } else {
                    P[k] = len;
                    k++;
                }
            }
        }

        int stringIndex = 0;
        int searchIndex = 0;
        int foundIndexes = -1;

        while (stringIndex < string.length()) {
            if (search_string.charAt(searchIndex) == string.charAt(stringIndex)) {
                searchIndex++;
                stringIndex++;
            }
            if (searchIndex == search_string.length()) {
                foundIndexes = stringIndex - searchIndex;
                Found(foundIndexes, StartTime);
                break;
            }

            else if (stringIndex < string.length() && search_string.charAt(searchIndex) != string.charAt(stringIndex)) {
                if (searchIndex != 0)
                    searchIndex = P[searchIndex - 1];
                else
                    stringIndex++;
            }
        } if (foundIndexes==-1)  Found(-1, StartTime);
    }

    static void BM_Search(char[] string, char[] search) {
        long StartTime = System.nanoTime();
        int n = string.length; // длина исходной строки
        int m = search.length; // длина искомой строки
        int P[] = new int[m];

        int k = 1;
        boolean flag;

        for (int i = m - 2; i >= 0; i--) {
            flag = true;
            for (int j = m - 2; j > i; j--) {
                if (search[i] == search[j]) {
                    P[i] = P[j];
                    flag = false;
                }
            }
            if (flag) P[i] = k;
            k++;
        }
        flag = true;
        for (int j = m - 2; j > 0; j--) {
            if (search[m-1] == search[j]) {
                P[m-1] = P[j];
                flag = false;
            }
        }
        if (flag) P[m - 1] = m;

        int position = m - 1;
        while (position < n - 1 ) {
            int check = m - 1;
            while (check > 0 && search[check] == string[position - m + check + 1]) check--;
            if (check <= 0) {
                Found(position - m + 1, StartTime);
                break;
            } else {
                int l = m;
                for (int i = m-1; i >= 0; i--) {
                    if (string[position] == search[i]) {
                        l = P[i];
                        break;
                    }
                }  position+=l;
            }
        } if (position >= n) Found(-1, StartTime);
    }

    static void Found(int id, long StartTime) {
        if (id == -1)   System.out.println("Объект не был найден!");
        else System.out.println("Объект был найден на позиции: " + ++id);
        System.out.println("Процесс занял " + ( System.nanoTime() - StartTime) * 0.000001 + " мс");
        System.out.println();

        Next();
    }

    static void Next() {
        System.out.println("Выберите дальнейшие действия: ");
        System.out.println("1. Осуществить ещё поиск");
        System.out.println("2. Завершить работу программы");
        int number = in.nextInt();
        System.out.println();
        if (number == 1) Program();
        else System.out.println("Программа завершила работу!");
    }
}
