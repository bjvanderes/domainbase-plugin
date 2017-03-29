package domainbase.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import datomic.Connection;
import datomic.Database;
import datomic.Peer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.Collection;

/**
 * Created by Brendan on 3/28/2017.
 */
public class DomainbaseQueryNavToolWindowFactory implements ToolWindowFactory {


    private JTextArea query;
    private JButton submitQuery;
    private JPanel domainbaseQueryNavToolWindowContent;
    private JTextField datomicUri;

    private ToolWindow domainbaseQueryNavToolWindow;


    public DomainbaseQueryNavToolWindowFactory() {
        submitQuery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                Connection conn = Peer.connect(datomicUri.getText().trim());
                Database db = conn.db();
                Collection results = Peer.query(query.getText(), db);
                JOptionPane.showMessageDialog(null, results);
            }
        });
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        domainbaseQueryNavToolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Connection conn = Peer.connect("datomic:ddb://us-east-1/Git4DomainbaseTable/plan");
        Database db = conn.db();
        Collection results = Peer.query("[:find ?plan-name (pull ?plan-t [*]) :where [_ :plan/name ?plan-name ?plan-t]]", db);
        JOptionPane.showMessageDialog(null, results);
        Content content = contentFactory.createContent(domainbaseQueryNavToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

    }

    public static  void main(String [] args) {
        Connection conn = Peer.connect("datomic:ddb://us-east-1/Git4DomainbaseTable/plan");
        Database db = conn.db();
        Collection results = Peer.query("[:find ?plan-name (pull ?plan-t [*]) :where [_ :plan/name ?plan-name ?plan-t]]", db);
        System.out.println(results);
    }
}