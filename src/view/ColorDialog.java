/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.Pixel;

/**
 * <p>Title: ColorDialog</p>
 * <p>Description: ... (JDialog)</p>
 * <p>Copyright: Copyright (c) 2003 Mohammed Elghaouat, Eric Paquette</p>
 * <p>Company: (�TS) - �cole de Technologie Sup�rieure</p>
 * @author unascribed
 * @version $Revision: 1.7 $
 */
public class ColorDialog extends JDialog {
	private JButton okButton;
	private RGBColorMediator rgbMediator;
	private CMYKColorMediator cmykMediator;
	private HSVColorMediator hsvMediator;
	private ActionListener okActionListener;
	private ColorDialogResult result;
	
	static public Pixel getColor(Frame owner, Pixel color, int imageWidths) {
		ColorDialogResult result = new ColorDialogResult(color);
		ColorDialog colorDialog = new ColorDialog(owner, result, imageWidths);
		colorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		colorDialog.pack();
		colorDialog.setVisible(true);
		if (result.isAccepted()) {
			return result.getPixel();
		} else {
			return null;
		}
	}

	ColorDialog(Frame owner, ColorDialogResult result, int imageWidths) {
		super(owner, true);
		this.result = result;
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel rgbPanel = createRGBPanel(result, imageWidths);
		tabbedPane.addTab("RGB", rgbPanel);

		JPanel cmykPanel = createCMYKPanel(result, imageWidths);
		tabbedPane.addTab("CMYK", cmykPanel);
		
		JPanel hsvPanel = createHSVPanel(result, imageWidths);
		tabbedPane.addTab("HSV", hsvPanel);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		AbstractAction okAction = new AbstractAction("OK") {
			public void actionPerformed(ActionEvent e) {
				ColorDialog.this.result.setAccepted(true);
				dispose();
			}
		};
		okButton = new JButton(okAction);
		buttonsPanel.add(okButton);
		AbstractAction cancelAction = new AbstractAction("Cancel") {
			public void actionPerformed(ActionEvent e) {
				ColorDialog.this.dispose();
			}
		};
		buttonsPanel.add(new JButton(cancelAction));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(tabbedPane);
		mainPanel.add(buttonsPanel);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	private JPanel createRGBPanel(ColorDialogResult result, int imageWidths) {	
		rgbMediator = new RGBColorMediator(result, imageWidths, 30);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		ColorSlider csRed = new ColorSlider("R:", result.getPixel().getRed(), rgbMediator.getRedImage());
		ColorSlider csGreen = new ColorSlider("G:", result.getPixel().getGreen(), rgbMediator.getGreenImage());
		ColorSlider csBlue = new ColorSlider("B:", result.getPixel().getBlue(), rgbMediator.getBlueImage());
		
		rgbMediator.setRedCS(csRed);
		rgbMediator.setGreenCS(csGreen);
		rgbMediator.setBlueCS(csBlue);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(csRed);
		panel.add(csGreen);
		panel.add(csBlue);
		
		return panel;
	}
	
	private JPanel createCMYKPanel(ColorDialogResult result, int imageWidths) {
		cmykMediator = new CMYKColorMediator(result, imageWidths, 30);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		ColorSlider csCyan = new ColorSlider("C:", cmykMediator.getCyan(), cmykMediator.getCyanImage());
		ColorSlider csMagenta = new ColorSlider("M:", cmykMediator.getMagenta(), cmykMediator.getMagentaImage());
		ColorSlider csJaune = new ColorSlider("Y:", cmykMediator.getJaune(), cmykMediator.getJauneImage());
		ColorSlider csNoir = new ColorSlider("N: ", cmykMediator.getNoir(), cmykMediator.getNoirImage());

		cmykMediator.setCyanCS(csCyan);
		cmykMediator.setJauneCS(csJaune);
		cmykMediator.setMagentaCS(csMagenta);
		cmykMediator.setNoirCS(csNoir);

		panel.add(csCyan);
		panel.add(csMagenta);
		panel.add(csJaune);
        panel.add(csNoir);

		return panel;
	}
	
	private JPanel createHSVPanel(ColorDialogResult result, int imageWidths) {
		hsvMediator = new HSVColorMediator(result, imageWidths, 30);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		ColorSlider csHue = new ColorSlider("H:", (hsvMediator.getHue()), hsvMediator.getHueImage());
		ColorSlider csSaturation = new ColorSlider("S:", (hsvMediator.getSaturation()), hsvMediator.getSaturationImage());
		ColorSlider csValue = new ColorSlider("V:", (hsvMediator.getValue()), hsvMediator.getValueImage());

		hsvMediator.setHueCS(csHue);
		hsvMediator.setSaturationCS(csSaturation);
		hsvMediator.setValueCS(csValue);

		panel.add(csHue);
		panel.add(csSaturation);
		panel.add(csValue);

		return panel;
	}
}

