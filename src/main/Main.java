package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        File inputFile = new File("src/res/input.jpg");
        Scanner input = new Scanner(System.in);
        try {
            BufferedImage inputImage = ImageIO.read(inputFile);

            int k;
            int height = inputImage.getHeight();
            int width = inputImage.getWidth();

            System.out.print("Enter the value of 'K' : ");
            k = input.nextInt();

            int[] k_n = new int[k];

            for (int i = 0; i < k; i++) {
                int randomY = new Random().nextInt(height);
                int randomX = new Random().nextInt(width);

                k_n[i] = inputImage.getRGB(randomX, randomY);
            }

            ArrayList[] cluster = new ArrayList[k];
            for (int i = 0; i < k; i++)
                cluster[i] = new ArrayList();

            boolean stop = false;

            while (!stop) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int p = inputImage.getRGB(x, y);

                        int a = (p >> 24) & 0xff;
                        int r = (p >> 16) & 0xff;
                        int g = (p >> 8) & 0xff;
                        int b = p & 0xff;


                        int minDistanceIndex = 0;
                        int _k = k_n[0];

                        int a1 = (_k >> 24) & 0xff;
                        int r1 = (_k >> 16) & 0xff;
                        int g1 = (_k >> 8) & 0xff;
                        int b1 = _k & 0xff;

                        int minDistance = (int) Math.sqrt((r1 - r) * (r1 - r) + (g1 - g) * (g1 - g) + (b1 - b) * (b1 - b));

                        for (int i = 1; i < k; i++) {
                            _k = k_n[i];

                            a1 = (_k >> 24) & 0xff;
                            r1 = (_k >> 16) & 0xff;
                            g1 = (_k >> 8) & 0xff;
                            b1 = _k & 0xff;

                            int distance = (int) Math.sqrt((r1 - r) * (r1 - r) + (g1 - g) * (g1 - g) + (b1 - b) * (b1 - b));
                            if (distance < minDistance) {
                                minDistance = distance;
                                minDistanceIndex = i;
                            }
                        }

                        cluster[minDistanceIndex].add(p);

                    }
                }

                for (int i = 0; i < k; i++) {
                    int sum = 0;
                    for (int j = 0; j < cluster[i].size(); j++)
                        sum += (int) cluster[i].get(j);

                    int avarage = k_n[i];

                    if (cluster[i].size() != 0)
                        avarage = sum / cluster[i].size();

                    if (avarage == k_n[i])
                        stop = true;

                    k_n[i] = avarage;
                }

                for (int i = 0; i < k; i++) {
                    if (!stop)
                        cluster[i].clear();
                }

            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int p = inputImage.getRGB(x, y);

                    for (int i = 0; i < k; i++) {
                        for (int j = 0; j < cluster[i].size(); j++)
                            if (p == (int) cluster[i].get(j))
                                inputImage.setRGB(x, y, k_n[i]);
                    }
                }
            }

            ImageIO.write(inputImage, "jpg", new File("src/res/output.jpg"));

        } catch (IOException e) {
            System.out.println("Faild to read image!");
        }

    }

}
