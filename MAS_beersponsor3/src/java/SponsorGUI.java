import java.awt.BorderLayout;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import jason.architecture.*;
import jason.asSemantics.ActionExec;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;

import javax.swing.*;

/** example of agent architecture's functions overriding */
public class SponsorGUI extends AgArch {

    JTextArea jt;
    JFrame    f;
    JButton patronize;

    public SponsorGUI() {
        jt = new JTextArea(10, 30);
        patronize = new JButton("Give more beer");
        patronize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Literal goal = ASSyntax.createLiteral("giving_beer");
                jt.append("\n\rGiving beer to my godson...");
                getTS().getC().addAchvGoal(goal, null);
                patronize.setEnabled(false);
            }
        });
        
        f = new JFrame("Sponsor agent");
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(BorderLayout.CENTER, new JScrollPane(jt));
        f.getContentPane().add(BorderLayout.SOUTH, patronize);
        f.pack();
        f.setVisible(true);
    }

    @Override
    public void act(ActionExec action, List<ActionExec> feedback) {
        if (action.getActionTerm().getFunctor().startsWith("thankyou")) {
            jt.append("\n\rThank you "+action.getActionTerm().getTerm(0)+"!");
            action.setResult(true);
            feedback.add(action);
            
            patronize.setEnabled(true); // enable GUI button
        } else {
            super.act(action,feedback); // send the action to the environment to be performed.
        }    	
    }

    @Override
    public void stop() {
        f.dispose();
        super.stop();
    }
}
