/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package com.timvisee.voxeltex.util.swing;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {

    /**
     * Unique serial version.
     */
    private static final long serialVersionUID = 9067989759873285740L;

    /**
     * Progress bar component.
     */
    private JProgressBar progressBar;

    /**
     * Status label.
     */
    private JLabel statusLabel;

    /**
     * True to show the cancel button, false to hide this button.
     */
    private boolean showCancelButton = false;

    /**
     * Define whether to show the progress.
     */
    private boolean showProgress = false;

    /**
     * The progress value.
     */
    private int progressValue = 0;

    /**
     * The progress maximum.
     */
    private int progressMax = 0;

    /**
     * Constructor.
     *
     * @param owner Owner window, or null.
     * @param title Progress dialog title.
     * @param showCancelButton True to show the cancel button, false if not.
     */
    public ProgressDialog(Window owner, String title, boolean showCancelButton) {
        // Construct the super class
        super(owner, title);

        // Store the cancel button property
        this.showCancelButton = showCancelButton;

        // Set the modality type if an owner is set
        if(owner != null)
            setModalityType(ModalityType.APPLICATION_MODAL);

        // Do not make this a modal
        setModal(false);

        // Build the dialog
        buildUi();

        // Configure the close button behaviour
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Make the form non resizable and keep it on top
        setResizable(false);
        setAlwaysOnTop(true);

        // Configure the preferred width of the dialog
        Dimension preferredDialogSize = getPreferredSize();
        preferredDialogSize.width = this.showCancelButton ? 350 : 300;
        setPreferredSize(preferredDialogSize);

        // Pack the dialog contents
        pack();

        // Set the dialog location to the center of the screen
        setLocationRelativeTo(owner);
    }

    /**
     * Constructor.
     *
     * @param owner Owner dialog, or null.
     * @param title Progress dialog title.
     * @param showCancelButton True to show the cancel button, false if not.
     */
    public ProgressDialog(Dialog owner, String title, boolean showCancelButton) {
        // Construct the super class
        super(owner, title);

        // Store the cancel button property
        this.showCancelButton = showCancelButton;

        // Set the modality type if an owner is set
        if(owner != null)
            setModalityType(ModalityType.APPLICATION_MODAL);

        // Do not make this a modal
        setModal(false);

        // Build the dialog
        buildUi();

        // Configure the close button behaviour
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Make the form non resizable and keep it on top
        setResizable(false);
        setAlwaysOnTop(true);

        // Configure the preferred width of the dialog
        Dimension preferredDialogSize = getPreferredSize();
        preferredDialogSize.width = this.showCancelButton ? 350 : 300;
        setPreferredSize(preferredDialogSize);

        // Pack the dialog contents
        pack();

        // Set the dialog location to the center of the screen
        setLocationRelativeTo(owner);
    }

    /**
     * Constructor.
     *
     * @param owner Owner window, or null.
     * @param title Progress dialog title.
     * @param showCancelButton True to show the cancel button.
     * @param status Initial status message.
     * @param show True to immediately show the progress dialog.
     */
    public ProgressDialog(Window owner, String title, boolean showCancelButton, String status, boolean show) {
        // Construct
        this(owner, title, showCancelButton);

        // Set the status
        setStatus(status);

        // Show the progress dialog if specified
        if(show)
            setVisible(true);
    }

    /**
     * Constructor.
     *
     * @param owner Owner dialog, or null.
     * @param title Progress dialog title.
     * @param showCancelButton True to show the cancel button.
     * @param status Initial status message.
     * @param show True to immediately show the progress dialog.
     */
    public ProgressDialog(Dialog owner, String title, boolean showCancelButton, String status, boolean show) {
        // Construct
        this(owner, title, showCancelButton);

        // Set the status
        setStatus(status);

        // Show the progress dialog if specified
        if(show)
            setVisible(true);
    }

    /**
     * Build the progress dialog UI.
     */
    private void buildUi() {
        // Create the base panel
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        // Create the status panel and label
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.LINE_AXIS));
        statusLabel = new JLabel("Initializing...", SwingConstants.LEADING);
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createHorizontalGlue());
        container.add(statusPanel);

        // Create the button panel with progress bar and buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 0, 2));
        progressBar = new JProgressBar();
        setShowProgress(true);
        buttonPanel.add(progressBar);
        progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, 20));
        if(this.showCancelButton) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton cancelButton = new JButton("Cancel");
            buttonPanel.add(cancelButton);
        }
        container.add(buttonPanel);

        // Add the container to the dialog
        add(container);

        // Pack everything
        pack();
    }

    /**
     * Set the title of the progression window
     */
    public void setTitle(String title) {
        super.setTitle(title);
    }

    /**
     * Set the status line in the progression frame
     * @param status Status text
     */
    public void setStatus(String status) {
        // Set the status label
        this.statusLabel.setText(status);

        // Force a repaint
        revalidate();
        forceRepaint();
    }

    /**
     * Check whether progress is shown.
     *
     * @return True if progress is shown, false if not.
     */
    public boolean isShowProgress() {
        return this.showProgress;
    }

    /**
     * Set whether progress is shown.
     *
     * @param showProgress True if progress is shown, false if not.
     */
    public void setShowProgress(boolean showProgress) {
        // Set the progress state
        this.showProgress = showProgress;

        // Update the progress bar
        this.progressBar.setIndeterminate(!showProgress);
    }

    /**
     * Get the current progress value.
     *
     * @return Progress value.
     */
    public int getProgressValue() {
        return this.progressValue;
    }

    /**
     * Set the current progress value.
     *
     * @param progressValue Progress value.
     */
    public void setProgressValue(int progressValue) {
        // Update the value
        this.progressValue = progressValue;

        // Force repaint
        forceRepaint();

        // Set the progress bar value
        this.progressBar.setValue(progressValue);
    }

    /**
     * Increase the progress value by one.
     */
    public void increaseProgressValue() {
        setProgressValue(getProgressValue() + 1);
    }

    /**
     * Get the progress maximum.
     *
     * @return Progress maximum.
     */
    public int getProgressMax() {
        return this.progressMax;
    }

    /**
     * Set the progress maximum.
     *
     * @param progressMax Progress maximum.
     */
    public void setProgressMax(int progressMax) {
        // Update the maximum
        this.progressMax = progressMax;

        // Set the progress bar maximum
        this.progressBar.setMaximum(progressMax);
    }

    /**
     * Force a repaint.
     */
    public void forceRepaint() {
        // Repaint the content pane
//        if(getContentPane() instanceof JComponent)
//            ((JComponent) getContentPane()).paintImmediately(((JComponent) getContentPane()).getVisibleRect());
    }
}
