package net.benjaminurquhart.utysave;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

public class UI extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -8751465600892495447L;
	
	public JProgressBar progressBar;
	public JButton importButton, exportButton, saveButton;
	public ImagePanel imageView;
	
	private final JFileChooser saveSelector = new JFileChooser(), imageSelector = new JFileChooser();
	
	private static UI instance;
	
	public static UI getInstance() {
		if(instance == null) {
			instance = new UI();
		}
		return instance;
	}
	
	private UI() {
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setValue(0);
		
		importButton = new JButton("Import");
		importButton.setActionCommand("import");
		importButton.addActionListener(this);
		
		exportButton = new JButton("Export");
		exportButton.setActionCommand("export");
		exportButton.addActionListener(this);
		
		saveButton = new JButton("Save");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		
		imageView = new ImagePanel();
		imageView.setImage(ExportWorker.render(Main.grid));
		
		setLayout(new BorderLayout());
		
		add(imageView, BorderLayout.PAGE_START);
		add(importButton, BorderLayout.LINE_START);
		add(saveButton, BorderLayout.CENTER);
		add(exportButton, BorderLayout.LINE_END);
		add(progressBar, BorderLayout.PAGE_END);
		
		saveSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		saveSelector.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return true;
			}

			@Override
			public String getDescription() {
				return "Undertale Yellow Save File (.sav)";
			}
		});
		
		imageSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		imageSelector.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return true;
			}

			@Override
			public String getDescription() {
				return "Image File";
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand()) {
		case "import": {
			disableControls();
			if(imageSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				ImportWorker worker = new ImportWorker(imageSelector.getSelectedFile(), Main.grid);
				worker.execute();
				disableControls();
			}
			else {
				onFinish(null);
			}
		} break;
		case "export": {
			ExportWorker worker = new ExportWorker(new File("id.png"), Main.grid);
			worker.execute();
			disableControls();
		} break;
		case "save": {
			try {
				disableControls();
				Main.save.put("SworksFlags", "sworks_id", "\"" + Main.grid.serialize() + "\"");
				Main.save.store(Main.file);
				onFinish(null);
				progressBar.setString("Saved");
			}
			catch(Exception e) {
				onFinish(e);
			}
		} break;
		}
	}
	
	private void disableControls() {
		importButton.setEnabled(false);
		exportButton.setEnabled(false);
		saveButton.setEnabled(false);
		
		Main.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void enableControls() {
		importButton.setEnabled(true);
		exportButton.setEnabled(true);
		saveButton.setEnabled(true);
		
		Main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void onFinish(Throwable e) {
		Main.frame.pack();
		if(e != null) {
			e.printStackTrace(System.out);
			Toolkit.getDefaultToolkit().beep();
			progressBar.setString("Internal error");
			JOptionPane.showMessageDialog(
					null, 
					"An internal error has occured.\nLeave an issue on Github:\n" 
					+ e.toString() 
					+ "\n" + Arrays.stream(e.getStackTrace()).map(String::valueOf).collect(Collectors.joining("\n\t")), 
					"Internal error", 
					JOptionPane.ERROR_MESSAGE
			);
		}
		else {
			progressBar.setString("");
		}
		enableControls();
		progressBar.setIndeterminate(false);
		progressBar.setValue(0);
	}
	
	public static void updateProgressBar(String state, String msg, int pos, int num) {
		JProgressBar progressBar = getInstance().progressBar;
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		progressBar.setString(state + " - " + msg);
		progressBar.setMaximum(num);
		progressBar.setMinimum(0);
		progressBar.setValue(pos);
	}
}
