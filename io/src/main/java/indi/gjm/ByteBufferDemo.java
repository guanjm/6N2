package indi.gjm;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * nio-字节数组
 * @author : guanjm
 * @date: 2020/12/17
 *
 */
public class ByteBufferDemo {

    public static void main(String[] args) {
        File file = new File(ByteBufferDemo.class.getResource("/ByteBufferDemo.txt").getPath());

        /*
            stream读写
            1、方式：输出流（inputStream）/输入流（outputStream）
            2、本地容器：一般通过byte[]装载数据
         */
        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileOutputStream outputStream = new FileOutputStream(file)) {

            //读数据
            byte[] bytes = new byte[1024];
            fileInputStream.read(bytes);
            System.out.println(new String(bytes));

            //写数据
            outputStream.write("FileOutputStream".getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
            文件是块设备，可以指定读写位置（来回读写）
         */
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "rw")) {
            //设置文件读写位置
            accessFile.seek(0);
            System.out.println(accessFile.readLine());

            accessFile.seek(0);
            accessFile.write("________________".getBytes());
            accessFile.seek(0);
            accessFile.write("RandomAccessFile".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
            channel + byteBuffer方式
            1、方式：通道（channel）= 输入流（inputStream）+ 输出流（outputStream）= 文件描述符（file description）
            2、本地容器：通过ByteBuffer装载数据
         */
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "rw")) {
            //jvm堆内（GC回收）
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //jvm堆外，java进程内存（需要置null，通过Cleaner回收，phantomReference使用）
            ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024);

            FileChannel channel = accessFile.getChannel();

            //文件读取数据 = inputStream.read
            channel.read(byteBuffer);
            //ByteBuffer设置为读状态
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                //从ByteBuffer读出数据
                System.out.print((char)byteBuffer.get());
            }

            //ByteBuffer设置为写状态
            byteBuffer.compact();
            byteBuffer.put("___Channel_IO___".getBytes());

            channel.position(0);
            //ByteBuffer设置为读状态
            byteBuffer.flip();
            //文件写入数据 = outputStream.write
            channel.write(byteBuffer);

            //强制写入存储设备 = outputStream.flush
            channel.force(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
            mmap = byteBuffer方式
            2、内存操作：直接mapByteBuffer
         */
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "rw")) {
            //映射文件内存（共享文件内存）
            MappedByteBuffer mapBuffer = accessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 1024);

            //ByteBuffer设置为读状态
            mapBuffer.flip();
            while (mapBuffer.hasRemaining()) {
                System.out.print(mapBuffer.get());
            }

            //ByteBuffer设置为写状态
            mapBuffer.compact();
            mapBuffer.put("____Mmap_IO____".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
