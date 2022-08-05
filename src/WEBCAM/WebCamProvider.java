package WEBCAM;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamResolution;

/**
 * @author Marcos Vinicius Angeli Costa.
 *
 * @mail marcosvinicios4132@gmail.com
 * 
 */
public class WebCamProvider implements WebcamDiscoveryListener {

	// Mostra a lista de webCans
	private JComboBox comboBox;

	// Tamanho da imagem que ser� salva
	private Dimension defaultDimension;

	// Liga/ Desliga/ Captura
	private Webcam webCam;

	// Controle para pparar os recursos
	private boolean executando = true;

	/*****************************************
	 * Contrutor que ativa o listener para ficar buscando por webCans plugadas no
	 * computador
	 *****************************************/
	public WebCamProvider(JComboBox comboBox) {
		this.comboBox = comboBox;
		for (Webcam webcam : Webcam.getWebcams()) {
			System.out.println("Webcam localizada: " + webcam.getName());
		}
		Webcam.addDiscoveryListener(this);
		System.out.println("\n\nPor favor conecte, ou desconecte webcam. esperando por eventos...");
	}

	/*****************************************
	 * 
	 * Listeners
	 *
	 *****************************************/
	@Override
	public void webcamFound(WebcamDiscoveryEvent event) {
		if (comboBox != null) {
			comboBox.removeAllItems();
			getListWebCam().stream().forEach(comboBox::addItem);
			comboBox.repaint();
		}
		System.out.format("Webcam conectada: %s \n", event.getWebcam().getName());
	}

	@Override
	public void webcamGone(WebcamDiscoveryEvent event) {
		if (comboBox != null) {
			comboBox.removeAllItems();
			getListWebCam().stream().forEach(comboBox::addItem);
			comboBox.repaint();
		}
		System.out.format("Webcam desconectada: %s \n", event.getWebcam().getName());
	}

	/***************************************
	 * 
	 * Retorna uma lista de webCam
	 * 
	 ***************************************/
	public List<Webcam> getListWebCam() {
		return Webcam.getWebcams();
	}

	/***************************************
	 * 
	 * Inicia a WebCam selecionada
	 * 
	 ***************************************/
	public void ligaWebCam(JLabel fotoWebCam, Webcam webCam) {
		try {

			if (this.webCam != null && this.webCam.isOpen()) {
				JOptionPane.showMessageDialog(null, "J� existe uma webCam Ligada");
			} else {
				defaultDimension = WebcamResolution.VGA.getSize(); // Resolu��o da WebCam

				// Se n�o informar web cam pega a default
				this.webCam = webCam == null ? Webcam.getDefault() : webCam;
				webCam.setViewSize(defaultDimension);

				initalizeVideoComponents(fotoWebCam).start();
			}

			// Resolu��o da webCam
			for (Dimension dimension : webCam.getViewSizes()) {
				System.out.println("Largura: " + dimension.getWidth() + " Altura: " + dimension.getHeight());
			}
		} catch (Exception e) {
			System.out.println("Ocorreu um erro ao iniciar a WebCam!");
		}
	}

	/***************************************
	 * 
	 * Inicializa a webCam
	 * 
	 ***************************************/
	public Thread initalizeVideoComponents(JLabel fotoWebCam) {
		return new Thread() {
			public void run() {
				fotoWebCam.setText("Iniciando...");
				webCam.open();
				inicializaVideo(fotoWebCam);
				fotoWebCam.setText("");
				executando = true;

			}
		};
	}

	/***************************************
	 * 
	 * Come�a a capturar a imagem
	 * 
	 ***************************************/
	private void inicializaVideo(JLabel fotoWebCam) {
		new Thread() {
			public void run() {
				while (true && executando) {
					Image image = null;
					ImageIcon icon = null;
					try {
						image = webCam.getImage();
						icon = new ImageIcon(image);
						icon.setImage(
								icon.getImage().getScaledInstance(fotoWebCam.getWidth(), fotoWebCam.getHeight(), 100));
						fotoWebCam.setIcon(icon);
						Thread.sleep(35);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "WebCam desconectada!");
						closeAndClearComponents(fotoWebCam).start();
						System.out.println("Desligou a C�mera");
					} finally {
						image = null;
						icon = null;
						System.gc();
					}
				}
			}
		}.start();
	}

	/***************************************
	 * 
	 * Captura uma foto
	 * 
	 ***************************************/
	public void capturaImagem(String path) {
		ByteArrayOutputStream buff = null;
		ByteArrayInputStream inputStream = null;
		BufferedImage imagem = null;
		BufferedImage newImg = null;
		Graphics2D graphics = null;
		try {
			// Converte a imagem para um array de bytes
			if (webCam == null) {
				JOptionPane.showMessageDialog(null, "WebCam desligada! " + path);
			} else {
				buff = new ByteArrayOutputStream();
				ImageIO.write(webCam.getImage(), "JPG", buff);
				byte[] bytes = buff.toByteArray();

				inputStream = new ByteArrayInputStream(bytes);
				imagem = ImageIO.read(inputStream);

				// Configura o tamanho da imagem
				int novaLargura = 500;
				int novaAltura = 500;

				// Configs da imagem a ser salva
				newImg = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_RGB);
				graphics = newImg.createGraphics();
				graphics.drawImage(imagem, 0, 0, novaLargura, novaAltura, null);

				// Caminho onde a imagem ser� salva
				ImageIO.write(newImg, "JPG", new File(path.trim().equals("") ? "./db/resources/captures/imagem.jpg"
						: path.contains(".jpg") ? path : path + ".jpg"));
				JOptionPane.showMessageDialog(null, path.trim().equals("") ? "Imagem salva em db/resources/captures/"
						: "Imagem salva com sucesso em: " + path + ".jpg");
			}
		} catch (FileNotFoundException e) {
			// Se o diret�rio n�o existir o sistema cria
			File foto = new File(path);
			boolean salvou = foto.mkdir();
			if (salvou) {
				JOptionPane.showMessageDialog(null, "Imagem salva com sucesso em: " + path + ".jpg");
			} else {
				JOptionPane.showMessageDialog(null, "Imagem n�o foi salva \nMotivo: " + e.getMessage());
			}
		} catch (Exception e) {
			// N�o permite que duas webCans sejam iniciadas ao mesmo tempo
			if (!webCam.isOpen())
				JOptionPane.showMessageDialog(null, "WebCam desligada!");
			else
				JOptionPane.showMessageDialog(null, "Imagem n�o foi salva \nMotivo: " + e.getMessage());
		} finally {
			buff = null;
			inputStream = null;
			imagem = null;
			newImg = null;
			graphics = null;
			System.gc();
		}

	}

	/***************************************
	 * 
	 * Fecha a WebCam
	 * 
	 ***************************************/
	public Thread closeAndClearComponents(JLabel fotoWebCam) {
		return new Thread() {
			public void run() {
				if (JOptionPane.showConfirmDialog(null, "Desconectar WebCam?", "WARNING",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					webCam.close();
					executando = false;
					fotoWebCam.setIcon(null);
					fotoWebCam.setText("Finalizado!");
				} else {
					return;
				}
			}
		};
	}
}
