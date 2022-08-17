package me.qscbm.uploadRes;

import io.github.mcchampions.DodoOpenJava.Utils;
import io.github.mcchampions.DodoOpenJava.api.ResourceApi;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class Main extends JFrame {
    public static File file;

    @Serial
    private static final long serialVersionUID = 1L;

    JButton btn = new JButton("浏览");

    JButton button = new JButton("上传");

    JTextField textField = new JTextField(30);
    JTextArea text = new JTextArea(3,45);

    JScrollPane Text = new JScrollPane(text);

    JLabel label = new JLabel("请选择文件：");// 标签
    JLabel t = new JLabel("输出：");// 标签

    public Main() {
        this.setBounds(400, 200, 600, 140);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("选择文件窗口");
        FlowLayout layout = new FlowLayout();// 布局
        this.setLayout(layout);
        layout.setAlignment(FlowLayout.CENTER);
        btn.addActionListener(new event1());
        button.addActionListener(new event2());
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        text.setLineWrap(true);
        Text.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(label);
        this.add(textField);
        this.add(btn);
        this.add(button);
        this.add(t,BorderLayout.CENTER);
        this.add(Text,BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new Main();
    }

    class event1 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FileNameExtensionFilter filename = new FileNameExtensionFilter("JPG、JPEG、GIF、PNG、WEBP", "PNG", "JPG", "JPEG", "GIF", "WEBP");
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(filename);
            chooser.showOpenDialog(Main.this);
            file = chooser.getSelectedFile();
            textField.setText(file.toString());
        }
    }

    class event2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String Parm;
            try {
                Parm = ResourceApi.uploadResource(Utils.Authorization("75477943", "NzU0Nzc5NDM.CVXvv719.2p-7fs7n2fOKottrW8JTCZFnLH9qjMWgV-vfh3Z2qGA"),file.getPath(), false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            text.setText(Parm);
        }
    }
}
