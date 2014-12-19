/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.viz.visit;

import java.io.IOException;

import gov.lbnl.visit.swt.VisItSwtWidget;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.part.IPageBookViewPage;

/**
 * This {@link IOConsole} subclass provides an mechanism for executing methods
 * from the VisIt Python API.
 * 
 * @author Taylor Patterson
 * 
 */
public class VisitPythonConsole extends IOConsole {

	/**
	 * The {@link VisItSwtWidget} for which this console provides an interface.
	 */
	private VisItSwtWidget visitWidget;

	/**
	 * The constructor
	 * 
	 * @see IOConsole(String, ImageDescriptor)
	 * 
	 * @param name
	 *            The name to display for this console
	 * @param imageDescriptor
	 *            The image to display for this console or <code>null</code>
	 * @param visitWidget
	 *            The {@link VisItSwtWidget} for which this console provides an
	 *            interface.
	 */
	public VisitPythonConsole(String name, ImageDescriptor imageDescriptor,
			VisItSwtWidget vizWidget) {
		// Call the parent constructor
		super(name, imageDescriptor);
		// Set the VisIt widget
		visitWidget = vizWidget;
	}

	/**
	 * @see IConsole#createPage(IConsoleView)
	 */
	@Override
	public IPageBookViewPage createPage(IConsoleView view) {
		return new VisitPythonConsolePage(this, view);
	}

	/**
	 * Execute the most recent user input in the this console. Parse the text in
	 * the console to grab the most recent command and send it off to the VisIt
	 * client.
	 */
	protected void executeCommand() {

		// Get the last command from the console Text
		String command = getDocument().get().trim();
		int start = Math.max(0, command.lastIndexOf("\n"));
		command = command.substring(start);
		// Call the VisIt widget method to process the command(s)
		if (!command.isEmpty()) {
			visitWidget.getViewerMethods().processCommands(command);
		}

		return;
	}

	/**
	 * Print the String ">>> " to indicate that the console is ready for input.
	 */
	public void writeInPrompt() {
		try {
			// Initiate an output stream to write to the console
			IOConsoleOutputStream outStream = newOutputStream();
			outStream.setActivateOnWrite(true);
			// Write ">>> " to the console
			outStream.write(">>> ");
			// Move the cursor to the end of the printed text
			// Close the output stream we are done using
			outStream.close();
		} catch (IOException e) {
			System.out.println("VisitPythonConsole Message: "
					+ "IOConsoleOutputStream error.");
			e.printStackTrace();
		}
	}
}