import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Window extends JFrame {

	private JPanel contentPane;
	private JLabel labelLevel;
	
	private JLabel[][] lt = new JLabel[9][13];
	private int[][] t = new int[9][13];
	
	private JLabel[] labelElem = new JLabel[4];
	
	private String iconJel = "FVKL.";
	private String[] iconNev = { "tree", "flower", "key", "lock", "null" };
	private ImageIcon[] icon = new ImageIcon[5];
	
	private final int N = 4;
		
	private int level = 1;
	private int elem = 0;

	private void betolt(int x) {
		Scanner be = null;
		try {
			be = new Scanner(new File("./levels/" + x + ".map"), "utf-8");
			for (int s=0; s<9; s++) { String sor = be.nextLine();
				for (int o=0; o<13; o++) {
					t[s][o] = iconJel.indexOf(sor.charAt(o));
					lt[s][o].setIcon(icon[t[s][o]]);
				}
			}
		}
		catch (Exception e) { JOptionPane.showMessageDialog(contentPane, e.toString(), "Error!", JOptionPane.ERROR_MESSAGE); }
		finally { if (be != null) be.close(); }
	}
	
	private void kattintas(int s, int o) {
		if (t[s][o] == elem) { t[s][o] = N; lt[s][o].setIcon(icon[N]); }
		else { t[s][o] = elem; lt[s][o].setIcon(icon[elem]); }
	}
	
	private void mentes(int x) {
		PrintWriter ki = null;
		File fki = new File("./levels/" + x + ".map");
		int valasz = JOptionPane.YES_OPTION;
		if (fki.exists()) { valasz = JOptionPane.showConfirmDialog(contentPane, "This level already exists. Overwrite it?"); }
		if (valasz == JOptionPane.YES_OPTION) {
			try { ki = new PrintWriter(fki, "utf-8");
				for (int s=0; s<9; s++) {
					for (int o=0; o<13; o++) ki.printf("%c", iconJel.charAt(t[s][o]));
					ki.printf("\r\n");
				}
			}
			catch (Exception e) { JOptionPane.showMessageDialog(contentPane, e.toString(), "Error!", JOptionPane.ERROR_MESSAGE); }
			finally { if (ki != null) ki.close(); }
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try { Window frame = new Window(); frame.setVisible(true); }
				catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	public Window() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/icons/tervezo.png")));
		setTitle("Editor");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
				
		JPanel panelKozep = new JPanel();
		panelKozep.setPreferredSize(new Dimension(10+13*64+10, 10+9*64+10));
		panelKozep.setBorder(new LineBorder(Color.BLACK));
		panelKozep.setBackground(Color.decode("#66cc66"));
		contentPane.add(panelKozep, BorderLayout.CENTER);
		panelKozep.setLayout(null);
		
		JPanel panelJobb = new JPanel();
		panelJobb.setPreferredSize(new Dimension(100, 10));
		contentPane.add(panelJobb, BorderLayout.EAST);
		panelJobb.setLayout(new BoxLayout(panelJobb, BoxLayout.Y_AXIS));
		
		JLabel labelPalya = new JLabel("Level");
		labelPalya.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelPalya.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelJobb.add(labelPalya);
		
		JPanel levelPanel = new JPanel();
		levelPanel.setMaximumSize(new Dimension(32767, 32));
		panelJobb.add(levelPanel);
		
		JLabel elozo = new JLabel("");
		elozo.setToolTipText("Previous level");
		elozo.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (level > 1) level--;
				labelLevel.setText(level + ".");
			}
		});
		elozo.setIcon(new ImageIcon(Window.class.getResource("/icons/left.png")));
		levelPanel.add(elozo);
		
		labelLevel = new JLabel("1.");
		labelLevel.setFont(new Font("Tahoma", Font.BOLD, 16));
		levelPanel.add(labelLevel);
		
		JLabel kovetkezo = new JLabel("");
		kovetkezo.setToolTipText("Next level");
		kovetkezo.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (level < 9) level++;
				labelLevel.setText(level + ".");
			}
		});
		kovetkezo.setIcon(new ImageIcon(Window.class.getResource("/icons/right.png")));
		levelPanel.add(kovetkezo);
		
		JPanel fajlPanel = new JPanel();
		fajlPanel.setMaximumSize(new Dimension(32767, 48));
		panelJobb.add(fajlPanel);
		
		JLabel labelOpen = new JLabel("");
		labelOpen.setToolTipText("Restore");
		labelOpen.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { betolt(level); } });
		labelOpen.setIcon(new ImageIcon(Window.class.getResource("/icons/open.png")));
		fajlPanel.add(labelOpen);
		
		JLabel labelSave = new JLabel("");
		labelSave.setToolTipText("Save level");
		labelSave.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { mentes(level); }});
		labelSave.setIcon(new ImageIcon(Window.class.getResource("/icons/save.png")));
		fajlPanel.add(labelSave);
		
		JLabel labelElemek = new JLabel("Elements");
		labelElemek.setBorder(new EmptyBorder(10, 0, 10, 0));
		labelElemek.setFont(new Font("Tahoma", Font.BOLD, 12));
		labelElemek.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelJobb.add(labelElemek);

		for (int i=0; i<5; i++) { icon[i] = new ImageIcon(Window.class.getResource("/icons/" + iconNev[i] + ".png")); }
		
		for (int i=0; i<4; i++) { int ii = i;
			labelElem[i] = new JLabel("");
			labelElem[i].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) { labelElem[ii].setBackground(Color.LIGHT_GRAY); }
				public void mouseExited(MouseEvent e) {
					if (elem == ii) labelElem[ii].setBackground(Color.decode("#66cc66"));
					else labelElem[ii].setBackground(new Color(240, 240, 240));
				}
				public void mousePressed(MouseEvent e) {
					labelElem[elem].setBackground(new Color(240, 240, 240));
					labelElem[ii].setBackground(Color.decode("#66cc66"));
					elem = ii;
				}
			});
			labelElem[i].setOpaque(true);
			labelElem[i].setBorder(new EmptyBorder(5, 5, 5, 5));
			labelElem[i].setBackground(new Color(240, 240, 240));
			labelElem[i].setIcon(icon[i]);
			labelElem[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			panelJobb.add(labelElem[i]);
		}
		labelElem[elem].setBackground(Color.decode("#66cc66"));
		
		Component verticalGlue = Box.createVerticalGlue();
		panelJobb.add(verticalGlue);
		
		JLabel labelKuka = new JLabel("");
		labelKuka.setToolTipText("Delete elements");
		labelKuka.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for (int s=0; s<9; s++) for (int o=0; o<13; o++) {
					t[s][o] = N; lt[s][o].setIcon(icon[N]);
				}
			}
		});
		labelKuka.setBorder(new EmptyBorder(0, 0, 10, 0));
		labelKuka.setIcon(new ImageIcon(Window.class.getResource("/icons/trash.png")));
		labelKuka.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelJobb.add(labelKuka);
		
		for (int s=0; s<9; s++) for (int o=0; o<13; o++) {
			int ss = s, oo = o; lt[s][o] = new JLabel("");
			lt[s][o].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) { lt[ss][oo].setBackground(Color.decode("#33ff33")); }
				public void mouseExited(MouseEvent e) { lt[ss][oo].setBackground(Color.decode("#66cc66")); }
				public void mousePressed(MouseEvent e) { kattintas(ss, oo); }
			});
			lt[s][o].setOpaque(true);
			lt[s][o].setIcon(icon[N]); t[s][o] = N;
			lt[s][o].setBackground(Color.decode("#66cc66"));
			lt[s][o].setBounds(10+o*64, 10+s*64, 64, 64);
			panelKozep.add(lt[s][o]);
		}
		
		pack(); betolt(1);
	}
	
}