package indi.gjm;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * NEW IO
 * @author : guanjm
 * @date: 2020/12/8
 *
 */
public class NIO {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket()) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
