package ui;

import java.io.Console;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CommonUI {
    private static Scanner sc = new Scanner(System.in);
    private static Console console = System.console();
    public static String inputString(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    public static String inputPassword(String message) {
        System.out.print(message);
        char[] chars = console.readPassword();
        return new String(chars);
    }

    public static int inputInteger(String message) {
        System.out.print(message);
        int num = 0;
        try {
            num = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("# 올바른 정수 입력값이 아닙니다!");
        } finally {
            sc.nextLine(); // try에서도(엔터 입력값), catch에서도(쓰레기 문자열 수거) nextLine()이 동작해야 함.
        }
        return num;
    }

    public static void makeLine() {
        System.out.println("=======================================================");
    }

    public static void startCustomerScreen() {
        System.out.println("===================== 환영합니다~! =====================");
        System.out.println("### 1. 구매/환불");
        System.out.println("### 2. 제품 조회");
        System.out.println("### 3. 고객 정보 조회");
        System.out.println("### 4. 회원가입");
        System.out.println("### 5. 관리자 외 출입금지");
        makeLine();
    }

}
