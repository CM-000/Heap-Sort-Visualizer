import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyPanel extends JPanel {

    ArrayList<Integer> intList = new ArrayList<Integer>();
    Random random = new Random();
    int rand;
    int temp;
    boolean sorted = false;
    private int linesDrawn = 0;
    int size = 300;
    private long startTime;
    private long elapsedTime;
    private double elapsedSeconds;
    int opCount;


    public MyPanel() {
    	
        this.setPreferredSize(new Dimension(size, size));

        for (int i = 0; i < size; i++) {
            rand = random.nextInt(0, size);
            intList.add(rand);
        }
        
        startTime = System.currentTimeMillis(); // Record the start time

        // Start a thread to sort the list after a short delay
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Delay for 500 milliseconds (adjust as needed)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            heapSort(intList);
            sorted = true;
            repaint(); // Trigger repaint after sorting
            
            long endTime = System.currentTimeMillis(); // Record the end time
            elapsedTime = endTime - startTime;
            elapsedSeconds = (double) elapsedTime / 1000.0;
            System.out.println("Sorting completed in " + elapsedSeconds + " seconds");
            System.out.println(opCount + " operations");
            
        }).start();
        
    }

    private void heapSort(ArrayList<Integer> list) {
        int n = list.size();

        // Build the max-heap
        buildMaxHeap(list);

        // Extract elements and maintain the heap
        for (int i = n - 1; i > 0; i--) {
            swap(list, 0, i);
            maxHeapify(list, i, 0);
        }
    }

    private void buildMaxHeap(ArrayList<Integer> list) {
        int n = list.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            maxHeapify(list, n, i);
        }
    }

    private void maxHeapify(ArrayList<Integer> list, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && list.get(left) > list.get(largest)) {
            largest = left;
        }

        if (right < n && list.get(right) > list.get(largest)) {
            largest = right;
        }

        if (largest != i) {
            swap(list, i, largest);
            maxHeapify(list, n, largest);
        }
        opCount++;
    }
    
    private void swap(ArrayList<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);

        // Pause to show the swap
        repaint(); // Trigger repaint to update the display
        try {
            Thread.sleep(1); // Adjust delay as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        opCount++;
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(Color.BLACK); // Fill the panel with black
        g2D.fillRect(0, 0, getWidth(), getHeight());
        
        g2D.setStroke(new BasicStroke(1));
        
        g2D.setColor(Color.YELLOW);
        if(!sorted) {
        	g2D.drawString("Time: (running)", 10, 20);
        } else {
            g2D.drawString("Time: " + elapsedSeconds + " s", 10, 20);
        }
        g2D.drawString("Operations: " + opCount, 10, 40);

        if (!sorted) {
            g2D.setColor(Color.WHITE);
            // Draw the unsorted white lines
            for (int i = 0; i < intList.size(); i++) {
                g2D.drawLine(i, getHeight(), i, intList.size() - intList.get(i));
            }
        } else {
            g2D.setColor(Color.WHITE);
            // Draw the sorted white lines
            for (int i = 0; i < intList.size(); i++) {
                g2D.drawLine(i, getHeight(), i, intList.size() - intList.get(i));
            }

            g2D.setColor(Color.RED);
            // Draw the sorted red lines one by one, creating a growing effect
            for (int i = 0; i < linesDrawn; i++) {
                g2D.drawLine(i, getHeight() - intList.get(i), i, getHeight());
            }
            
            
            if (linesDrawn < intList.size()) {
                linesDrawn++; // Increase the number of lines drawn
                repaint(); // Trigger repaint to continue the animation
            }
        }
        
    }
}
