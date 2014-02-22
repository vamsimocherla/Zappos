import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ZappGui extends JFrame {
	
	public ZappGui() {
		JComponent newContentPane = new GuiPanel();
		newContentPane.setOpaque(true);
		setContentPane(newContentPane);
		
		setTitle("Search for Gifts on Zappos.com");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	private class GuiPanel extends JPanel {
		private JLabel quantityLabel, costLabel;
		private JTextField quantityField, costField;
		private JButton searchButton;
		private JPanel drawingPane;
		ZappProduct[][] searchResults;
		
		public GuiPanel() {
			super(new BorderLayout());
			
			// create a label for Quantity
			quantityLabel = new JLabel();
			quantityLabel.setVisible(true);
			quantityLabel.setText("Quantity:");
			
			// create a field for Quantity
			quantityField = new JTextField();
			quantityField.setVisible(true);
			quantityField.setPreferredSize(new Dimension(50, 20));
			
			// create a label for Cost
			costLabel = new JLabel();
			costLabel.setVisible(true);
			costLabel.setText("Cost in $:");
			
			// create a field for Cost
			costField = new JTextField();
			costField.setVisible(true);
			costField.setPreferredSize(new Dimension(50, 20));
			
			// create a button to Search
			searchButton = new JButton("Search");
			searchButton.setVisible(true);

			// add action listener to button
			searchButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					updateResults();
				}
			});
			
			// add the above to a panel
			JPanel inputPanel = new JPanel();
			inputPanel.add(quantityLabel);
			inputPanel.add(quantityField);
			inputPanel.add(costLabel);
			inputPanel.add(costField);
			inputPanel.add(searchButton);
			
			// setting up the drawing area
			drawingPane = new DrawingPane();
			
			// put the drawing area in a scroll pane
			JScrollPane scroller = new JScrollPane(drawingPane);
			scroller.setPreferredSize(new Dimension(800, 500));
			
			// layout the components
			add(inputPanel, BorderLayout.PAGE_START);
			add(scroller, BorderLayout.CENTER);
		}

		public class DrawingPane extends JPanel {
			protected void paintComponent(Graphics g) {
	
				super.paintComponent(g);
				
				if(searchResults != null) {
					// display the top 'numResults'
					int numResults = 5;
					for(int j=0;j<numResults;j++) {
						// print the numbering
						g.setFont(new Font("Helvetica", Font.BOLD, 14));
						g.drawString(String.valueOf(j+1), 20, 60+150*j);
						
						// display the images
						int quantity = searchResults[j].length;
						double totalSum = 0;
						BufferedImage[] image = new BufferedImage[quantity];
						for(int i=0;i<quantity;i++) {
							try {
								// compute total cost of the products
								totalSum += Double.parseDouble(searchResults[j][i].getPrice().substring(1));
								// read the product's thumbnail image
								URL url = new URL(searchResults[j][i].getThumbnailImageUrl());
								image[i] = ImageIO.read(url);
								// draw the image
								g.drawImage(image[i], 40+175*i, 50+150*j, null);
								// truncate the product's name to a max of 20 characters
								if(searchResults[j][i].getProductName().length() > 20) {
									g.setFont(new Font("Helvetica", Font.BOLD, 14));
									g.drawString(searchResults[j][i].getProductName().substring(0, 20), 40+175*i, 175+150*j);
								}
								else {
									g.setFont(new Font("Helvetica", Font.BOLD, 14));
									g.drawString(searchResults[j][i].getProductName(), 40+175*i, 175+150*j);
								}
								// print the cost of each product
								g.setFont(new Font("Helvetica", Font.BOLD, 14));
								g.drawString("Cost = " + searchResults[j][i].getPrice(), 40+175*i, 190+150*j);
							}
							catch(IOException ex) {
								System.out.println(ex.getMessage());
							}
							g.drawLine(175*i, 195+150*j, 175*quantity, 195+150*j);
						}
						// print the total cost of the products
						g.setFont(new Font("Helvetica", Font.BOLD, 14));
						g.drawString("Total Cost of the products = $" +
								String.valueOf(Double.valueOf(new DecimalFormat("###.##").format(totalSum))),
								40+175*quantity,
								100+150*j);
					}
				}
			}
		}
		
		private void updateResults() {
			// parse the user input
			int quantity = 0;
			double cost = 0;
			try {
				quantity = Integer.parseInt(quantityField.getText());
			} catch (Exception ex) {
				System.out.println("Bad value for Quantity");
			}
			
			try {
				cost = Double.parseDouble(costField.getText());
			} catch (Exception ex) {
				System.out.println("Bad value for Cost");
			}
			
			// get the list of products whose value is close to 'cost'
			searchResults = ZappGifts.searchForGifts(quantity, cost);
			
			drawingPane.setPreferredSize(new Dimension(175*(quantity+2),150*7));
			drawingPane.repaint();
			drawingPane.revalidate();
		}
	}
}