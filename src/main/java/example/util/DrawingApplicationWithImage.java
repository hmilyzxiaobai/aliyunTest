package example.util;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class DrawingApplicationWithImage extends JFrame {
    private JPanel drawingPanel;
    private JPanel controlPanel;
    private JButton clearButton;
    private JButton textButton;
    private JButton saveButton;
    private JButton imageButton;
    private JSlider thicknessSlider;
    private JColorChooser colorChooser;

    private Color currentColor = Color.BLACK;
    private int currentThickness = 3;
    private boolean isDrawing = false;
    private boolean isTextMode = false;
    private boolean isImageMode = false;
    private Point lastPoint;

    private BufferedImage bufferImage;
    private List<DrawnText> textElements = new ArrayList<>();
    private List<InsertedImage> insertedImages = new ArrayList<>();
    private InsertedImage currentImageBeingPlaced = null;

    public DrawingApplicationWithImage() {
        setTitle("Java 绘图与文本输入 - 支持图像插入");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();

        // 初始化缓冲区图像
        bufferImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 800, 600);
        g2d.dispose();
    }

    private void initComponents() {
        // 绘图面板
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 绘制缓冲区内容
                g.drawImage(bufferImage, 0, 0, null);

                // 绘制所有插入的图像
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for (InsertedImage img : insertedImages) {
                    g2d.drawImage(img.getImage(), img.getX(), img.getY(), img.getWidth(), img.getHeight(), null);
                }

                // 绘制所有文本元素
                for (DrawnText text : textElements) {
                    g2d.setColor(text.getColor());
                    g2d.setFont(new Font("Arial", Font.PLAIN, text.getSize()));
                    g2d.drawString(text.getText(), text.getX(), text.getY());
                }

                // 绘制当前正在放置的图像（如果有）
                if (currentImageBeingPlaced != null) {
                    g2d.drawImage(currentImageBeingPlaced.getImage(),
                            currentImageBeingPlaced.getX(),
                            currentImageBeingPlaced.getY(),
                            currentImageBeingPlaced.getWidth(),
                            currentImageBeingPlaced.getHeight(),
                            null);
                }
            }
        };

        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setPreferredSize(new Dimension(800, 600));

        // 鼠标监听器
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isTextMode) {
                    handleTextInput(e);
                } else if (isImageMode && currentImageBeingPlaced != null) {
                    // 放置图像
                    insertedImages.add(currentImageBeingPlaced);
                    currentImageBeingPlaced = null;
                    isImageMode = false;
                    imageButton.setBackground(controlPanel.getBackground());
                    drawingPanel.repaint();

                    // 更新缓冲区
                    updateBufferImage();
                } else if (!isImageMode) {
                    // 绘图模式
                    isDrawing = true;
                    lastPoint = e.getPoint();

                    // 在按下时画一个点
                    Graphics2D g2d = bufferImage.createGraphics();
                    g2d.setColor(currentColor);
                    g2d.setStroke(new BasicStroke(currentThickness));
                    g2d.fillOval(e.getX() - currentThickness/2, e.getY() - currentThickness/2, currentThickness, currentThickness);
                    g2d.dispose();
                    drawingPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawing = false;
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing && !isTextMode && !isImageMode) {
                    Graphics2D g2d = bufferImage.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(currentColor);
                    g2d.setStroke(new BasicStroke(currentThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine(lastPoint.x, lastPoint.y, e.getX(), e.getY());
                    g2d.dispose();

                    lastPoint = e.getPoint();
                    drawingPanel.repaint();
                } else if (isImageMode && currentImageBeingPlaced != null) {
                    // 移动图像位置
                    currentImageBeingPlaced.setX(e.getX());
                    currentImageBeingPlaced.setY(e.getY());
                    drawingPanel.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (isImageMode && currentImageBeingPlaced != null) {
                    // 移动图像位置
                    currentImageBeingPlaced.setX(e.getX());
                    currentImageBeingPlaced.setY(e.getY());
                    drawingPanel.repaint();
                }
            }
        });

        // 控制面板
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // 清除按钮
        clearButton = new JButton("清除画布");
        clearButton.addActionListener(e -> clearCanvas());

        // 文本按钮
        textButton = new JButton("文本模式");
        textButton.addActionListener(e -> {
            isTextMode = !isTextMode;
            isImageMode = false;
            textButton.setBackground(isTextMode ? Color.GREEN : controlPanel.getBackground());
            imageButton.setBackground(controlPanel.getBackground());
            drawingPanel.setCursor(isTextMode ? Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR) :
                    Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        });

        // 图像按钮
        imageButton = new JButton("插入图像");
        imageButton.addActionListener(e -> {
            if (!isImageMode) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("选择要插入的图像");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) {
                        String name = f.getName().toLowerCase();
                        return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                                name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".bmp");
                    }
                    public String getDescription() {
                        return "图像文件 (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
                    }
                });

                if (fileChooser.showOpenDialog(DrawingApplicationWithImage.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage img = ImageIO.read(fileChooser.getSelectedFile());
                        if (img != null) {
                            // 限制图像大小，最大宽度或高度为300像素
                            int width = img.getWidth();
                            int height = img.getHeight();
                            double scale = 1.0;

                            if (width > 300 || height > 300) {
                                if (width > height) {
                                    scale = 300.0 / width;
                                } else {
                                    scale = 300.0 / height;
                                }
                                width = (int)(width * scale);
                                height = (int)(height * scale);
                            }

                            currentImageBeingPlaced = new InsertedImage(img, 0, 0, width, height);
                            isImageMode = true;
                            isTextMode = false;
                            imageButton.setBackground(Color.GREEN);
                            textButton.setBackground(controlPanel.getBackground());
                            drawingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(DrawingApplicationWithImage.this,
                                "加载图像失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // 取消图像插入
                isImageMode = false;
                currentImageBeingPlaced = null;
                imageButton.setBackground(controlPanel.getBackground());
                drawingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }
        });

        // 保存按钮
        saveButton = new JButton("保存图像");
        saveButton.addActionListener(e -> saveImage());

        // 线条粗细滑块
        thicknessSlider = new JSlider(1, 20, currentThickness);
        thicknessSlider.setMajorTickSpacing(5);
        thicknessSlider.setMinorTickSpacing(1);
        thicknessSlider.setPaintTicks(true);
        thicknessSlider.setPaintLabels(true);
        thicknessSlider.addChangeListener(e -> currentThickness = thicknessSlider.getValue());

        // 颜色选择器
        colorChooser = new JColorChooser(currentColor);
        colorChooser.setPreviewPanel(new JPanel());
        colorChooser.getSelectionModel().addChangeListener(e -> currentColor = colorChooser.getColor());

        // 添加组件到控制面板
        controlPanel.add(clearButton);
        controlPanel.add(textButton);
        controlPanel.add(imageButton);
        controlPanel.add(saveButton);
        controlPanel.add(new JLabel("线条粗细:"));
        controlPanel.add(thicknessSlider);
        controlPanel.add(new JLabel("颜色:"));
        controlPanel.add(colorChooser);

        // 添加到主窗口
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void handleTextInput(MouseEvent e) {
        String text = JOptionPane.showInputDialog(DrawingApplicationWithImage.this, "输入文本:", "文本输入", JOptionPane.PLAIN_MESSAGE);
        if (text != null && !text.trim().isEmpty()) {
            textElements.add(new DrawnText(text, e.getX(), e.getY(), currentColor, 20));
            drawingPanel.repaint();

            // 保存到缓冲区
            Graphics2D g2d = bufferImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(currentColor);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.drawString(text, e.getX(), e.getY());
            g2d.dispose();
        }
    }

    private void clearCanvas() {
        Graphics2D g2d = bufferImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, bufferImage.getWidth(), bufferImage.getHeight());
        g2d.dispose();
        textElements.clear();
        insertedImages.clear();
        drawingPanel.repaint();
    }

    private void updateBufferImage() {
        Graphics2D g2d = bufferImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, bufferImage.getWidth(), bufferImage.getHeight());

        // 重绘所有元素到缓冲区
        for (InsertedImage img : insertedImages) {
            g2d.drawImage(img.getImage(), img.getX(), img.getY(), img.getWidth(), img.getHeight(), null);
        }

        for (DrawnText text : textElements) {
            g2d.setColor(text.getColor());
            g2d.setFont(new Font("Arial", Font.PLAIN, text.getSize()));
            g2d.drawString(text.getText(), text.getX(), text.getY());
        }

        g2d.dispose();
    }

    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存图像");

        if (fileChooser.showSaveDialog(DrawingApplicationWithImage.this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getParentFile(), file.getName() + ".png");
                }

                // 创建一个包含所有内容的图像
                BufferedImage image = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = image.createGraphics();
                drawingPanel.paint(g2d);
                g2d.dispose();

                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(DrawingApplicationWithImage.this, "图像保存成功!", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(DrawingApplicationWithImage.this, "保存失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 内部类表示绘制的文本
    private static class DrawnText {
        private String text;
        private int x, y;
        private Color color;
        private int size;

        public DrawnText(String text, int x, int y, Color color, int size) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
            this.size = size;
        }

        public String getText() { return text; }
        public int getX() { return x; }
        public int getY() { return y; }
        public Color getColor() { return color; }
        public int getSize() { return size; }
    }

    // 内部类表示插入的图像
    private static class InsertedImage {
        private BufferedImage image;
        private int x, y;
        private int width, height;

        public InsertedImage(BufferedImage image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public BufferedImage getImage() { return image; }
        public int getX() { return x; }
        public int getY() { return y; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }

        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrawingApplicationWithImage app = new DrawingApplicationWithImage();
            app.setVisible(true);
        });
    }
}