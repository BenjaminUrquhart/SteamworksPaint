package net.benjaminurquhart.utysave;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.ini4j.Ini;

import net.benjaminurquhart.utysave.ds.DSGrid;
import net.benjaminurquhart.utysave.ds.DSUtil;

public class UI extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -8751465600892495447L;
	private static final Object lock = new Object();
	
	public JProgressBar progressBar;
	public JButton importButton, exportButton, saveButton;
	public ImagePanel imageView;
	
	private Ini save;
	private File file;
	private DSGrid grid;
	private boolean ready;
	
	private final JFileChooser saveSelector = new JFileChooser(), imageSelector = new JFileChooser();
	
	private static volatile UI instance;
	
	public static UI getInstance() {
		if(instance == null) {
			synchronized(lock) {
				if(instance == null) {
					instance = new UI();
				}
			}
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
		
		file = new File(System.getenv("LOCALAPPDATA") + "/Undertale_Yellow/Save.sav");
		while(!file.exists() || file.isDirectory()) {
			if(saveSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				file = saveSelector.getSelectedFile();
			}
			else {
				System.exit(0);
				return;
			}
		}
		imageView = new ImagePanel();
		
		setLayout(new BorderLayout());
		
		add(imageView, BorderLayout.PAGE_START);
		add(importButton, BorderLayout.LINE_START);
		add(saveButton, BorderLayout.CENTER);
		add(exportButton, BorderLayout.LINE_END);
		add(progressBar, BorderLayout.PAGE_END);
		
		ready = true;
		GenericWorker worker = new GenericWorker("Loading...", () -> {
			try {
				disableControls();
				save = new Ini(file);
				String idData = save.get("SworksFlags", "sworks_id");
				idData = idData.substring(1, idData.length() - 1);
				ByteBuffer buff = ByteBuffer.wrap(DSUtil.decodeHexString(idData));
				buff.order(ByteOrder.LITTLE_ENDIAN);
				buff.getInt();
				grid = new DSGrid(buff);
				imageView.setImage(ExportWorker.render(grid));
				onFinish(null);
			}
			catch(Exception e) {
				onFinish(e);
			}
		});
		worker.execute();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand()) {
		case "import": {
			disableControls();
			if(imageSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				ImportWorker worker = new ImportWorker(imageSelector.getSelectedFile(), grid);
				worker.execute();
				disableControls();
			}
			else {
				onFinish(null);
			}
		} break;
		case "export": {
			ExportWorker worker = new ExportWorker(new File("id.png"), grid);
			worker.execute();
			disableControls();
		} break;
		case "save": {
			GenericWorker worker = new GenericWorker("Saving...", () -> {
				try {
					disableControls();
					save.put("SworksFlags", "sworks_id", "\"" + grid.serialize() + "\"");
					save.store(file);
					onFinish(null);
					progressBar.setString("Saved");
				}
				catch(Exception e) {
					onFinish(e);
				}
			});
			worker.execute();
		} break;
		}
	}
	
	private void disableControls() {
		if(ready) {
			importButton.setEnabled(false);
			exportButton.setEnabled(false);
			saveButton.setEnabled(false);
			
			Main.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}
	
	private void enableControls() {
		if(ready) {
			importButton.setEnabled(true);
			exportButton.setEnabled(true);
			saveButton.setEnabled(true);
			
			Main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
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
}
