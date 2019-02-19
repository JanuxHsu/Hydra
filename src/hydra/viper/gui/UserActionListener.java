package hydra.viper.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileFilter;

public class UserActionListener implements KeyListener {

	ViperGuiController controller;

	Integer counter = 0;

	public UserActionListener(ViperGuiController controller) {
		this.controller = controller;

	}

	class CustomFileFilter implements FileFilter {

		String keyword;

		public CustomFileFilter(String currentInputStr) {
			this.keyword = currentInputStr;
		}

		@Override
		public boolean accept(File pathname) {

			if (this.keyword == null || pathname.getName().toLowerCase().startsWith(this.keyword)) {

				if (!pathname.isHidden()) {
					return true;
				}

			}
			return false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

		switch (e.getExtendedKeyCode()) {
		case KeyEvent.VK_TAB:

			String keyword = this.controller.gui.getAutoCompleteKeyword();

			FileFilter filter = new CustomFileFilter(keyword);
			File[] subfiles = this.controller.currentWorkingDir.listFiles(filter);

			File autoTo = subfiles[this.counter];

			String newWord = "";
			if (autoTo.isDirectory()) {
				newWord = autoTo.getName() + File.separatorChar;
			} else {
				newWord = autoTo.getName();
			}

			this.controller.gui.autoComplete(newWord);

			this.counter++;
			this.counter = this.counter > subfiles.length - 1 ? 0 : counter;

			break;

		default:
			break;
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
