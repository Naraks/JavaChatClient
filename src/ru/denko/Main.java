package ru.denko;

/** Simple echo chat console client
 *  Supports sending messages to a specific client
 *  @author Rudenko Sergey
 *  @author naraks@yandex.ru
 *  @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        new Thread(new Client("localhost", 18989, "Serg")).start();

    }
}
