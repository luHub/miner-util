package core;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

import java.io.File;
import java.util.List;

/**
 * Created by mey on 10/30/2016.
 */
public class ExampleDistiller {

    public static void main(String[] args) {

        System.out.println("HERE");

        //One folder for the
        File left = new File("C:\\Users\\mey\\workspace\\TestDistiler\\v1\\Main.java");

        File right = new File("C:\\Users\\mey\\workspace\\TestDistiler\\v2\\Main.java");

        FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
        try {
            distiller.extractClassifiedSourceCodeChanges(left, right);
        } catch (Exception e) {
			/*
			 * An exception most likely indicates a bug in ChangeDistiller.
			 * Please file a bug report at
			 * https://bitbucket.org/sealuzh/tools-changedistiller/issues and
			 * attach the full stack trace along with the two files that you
			 * tried to distill.
			 */
            System.err.println("Warning: error while change distilling. " + e.getMessage());
        }

        List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
        if (changes != null) {
            System.out.println("*");
            System.out.println("Changes: "+changes);
            for (SourceCodeChange change : changes) {
                // see Javadocs for more information
                System.out.println("*");
                System.out.println("**");
                System.out.println("****");
        		System.out.println("Changes: " + change);
        		System.out.println("Change Parent: " + change.getParentEntity());
        		System.out.println("Change Parent Type: " + change.getParentEntity().getType());
        		System.out.println("Change Parent Label: " + change.getParentEntity().getLabel());
        		System.out.println("Change Parent End Position: " + change.getParentEntity().getEndPosition());
        		System.out.println("Change Parent Start Position: " + change.getParentEntity().getStartPosition());
        		System.out.println("Change Parent Unique Name: " + change.getParentEntity().getUniqueName());
        		System.out.println("Change Parent Assosc Entities: " + change.getParentEntity().getAssociatedEntities());
        		System.out.println("Change Parent Source Range: " + change.getParentEntity().getSourceRange());
        		System.out.println("Changes Entity: " + change.getChangedEntity());
        		System.out.println("Changes Entity Unique Name: " + change.getChangedEntity().getUniqueName());
        		System.out.println("Changes Entity Source Range: " + change.getChangedEntity().getSourceRange());
        		System.out.println("Changes Entity Modifiers: " + change.getChangedEntity().getModifiers());
        		System.out.println("Changes Entity Associated Entities: " + change.getChangedEntity().getAssociatedEntities());
        		System.out.println("Changes Entity  Type: " + change.getChangedEntity().getType());
        		System.out.println("Changes Type: " + change.getChangeType());
        		System.out.println("Changes Label: " + change.getLabel());
//        		System.out.println("Changes Label: " + change.getParentEntity().get);
        		System.out.println("****");
                System.out.println("**");
                System.out.println("*");
                
                
                if ((change.getChangeType().equals(ChangeType.STATEMENT_INSERT)
        				|| change.getChangeType().equals(ChangeType.ALTERNATIVE_PART_INSERT))
                		&& (change.getChangedEntity().getType().equals(JavaEntityType.ELSE_STATEMENT)
        						|| change.getChangedEntity().equals(JavaEntityType.ELSE_STATEMENT))) {
                	System.out.println("*****************ELSE INSERT***********************");
                }

        		// Number of deleted else-parts from a method body over all revisions
        		if ((change.getChangeType().equals(ChangeType.STATEMENT_DELETE)
        				|| change.getChangeType().equals(ChangeType.ALTERNATIVE_PART_DELETE))
        				&& (change.getChangedEntity().getType().equals(JavaEntityType.ELSE_STATEMENT)
        						|| change.getChangedEntity().equals(JavaEntityType.ELSE_STATEMENT))) {
                	System.out.println("******************ELSE DELETE***********************");

        		}
                
        		
            }
            System.out.println("End");
        }else{
            System.out.println("no changes");
        }
    }
}
