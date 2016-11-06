package changedistillertest;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.event.ChangeEvent;

import org.junit.Test;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import core.MethodBugLevelCollector;
import core.MethodBugLevelCollector.FileInfoHelper;
import junit.framework.Assert;
import miner_pojos.ChangeMetric;
import miner_pojos.CommitInfov2;

public class ChangeDistillerTests {

	@Test
	public void removeElseFromMethodTest() throws URISyntaxException {
		
		MethodBugLevelCollector methodBugLevelCollector = new MethodBugLevelCollector();
		FileInfoHelper fileInfo = methodBugLevelCollector.new FileInfoHelper();
		CommitInfov2 commitInfo = new CommitInfov2();
		fileInfo.setCommitInfo(commitInfo);
		ChangeMetric changeMetric = new ChangeMetric();

		URI uriV1 = this.getClass().getClassLoader().getResource("comparetest/v1/MethodLevelChangeElseRemoved.java").toURI();
		File left = Paths.get(uriV1).toFile();
		
		URI uriV2 = this.getClass().getClassLoader().getResource("comparetest/v2/MethodLevelChangeElseRemoved.java").toURI();
		File right = Paths.get(uriV2).toFile();
		
		FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
		try {
			distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch (Exception e) {
			System.err.println("Warning: error while change distilling. " + e.getMessage());
		}
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if (changes != null) {
			print(changes);
			changes.stream().filter((change) -> change.getRootEntity().getType().equals(JavaEntityType.METHOD)||change.getParentEntity().getType().equals(JavaEntityType.METHOD))
			.forEach((methodChange) -> {
				changeMetric.updateMetricsPerChange(fileInfo, methodChange);
			});
		} else {
			Assert.fail();
		}
		Assert.assertTrue(changeMetric.getElseDeleted()==1);
		Assert.assertFalse(changeMetric.getElseAdded()>0);
	}
	
	@Test
	public void removeElseNestedInConditionalExpressionTest() throws URISyntaxException{
		MethodBugLevelCollector methodBugLevelCollector = new MethodBugLevelCollector();
		FileInfoHelper fileInfo = methodBugLevelCollector.new FileInfoHelper();
		CommitInfov2 commitInfo = new CommitInfov2();
		fileInfo.setCommitInfo(commitInfo);
		ChangeMetric changeMetric = new ChangeMetric();

		URI uriV1 = this.getClass().getClassLoader().getResource("comparetest/v1/MethodLevelNestedInConditionalExpressionOneLevelRemoving.java").toURI();
		File left = Paths.get(uriV1).toFile();
		
		URI uriV2 = this.getClass().getClassLoader().getResource("comparetest/v2/MethodLevelNestedInConditionalExpressionOneLevelRemoving.java").toURI();
		File right = Paths.get(uriV2).toFile();
		
		FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
		try {
			distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch (Exception e) {
			System.err.println("Warning: error while change distilling. " + e.getMessage());
		}
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if (changes != null) {
			changes.stream().filter((change) -> change.getRootEntity().getType().equals(JavaEntityType.METHOD)  ||  change.getParentEntity().getType().equals(JavaEntityType.METHOD))
			.forEach((methodChange) -> {
				changeMetric.updateMetricsPerChange(fileInfo, methodChange);
			});
		} else {
			Assert.fail();
		}
		Assert.assertTrue(changeMetric.getElseDeleted()==1);
		Assert.assertFalse(changeMetric.getElseAdded()>0);
	}
	
	//TODO
	@Test
	public void methodChangeNameTest() throws URISyntaxException{
		MethodBugLevelCollector methodBugLevelCollector = new MethodBugLevelCollector();
		FileInfoHelper fileInfo = methodBugLevelCollector.new FileInfoHelper();
		CommitInfov2 commitInfo = new CommitInfov2();
		fileInfo.setCommitInfo(commitInfo);
		ChangeMetric changeMetric = new ChangeMetric();

		URI uriV1 = this.getClass().getClassLoader().getResource("comparetest/v1/MethodChangeName.java").toURI();
		File left = Paths.get(uriV1).toFile();
		
		URI uriV2 = this.getClass().getClassLoader().getResource("comparetest/v2/MethodChangeName.java").toURI();
		File right = Paths.get(uriV2).toFile();
		
		FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
		try {
			distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch (Exception e) {
			System.err.println("Warning: error while change distilling. " + e.getMessage());
		}
		
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if (changes != null) {
			
			print(changes);
			
			changes.stream().filter((change) -> change.getRootEntity().getType().equals(JavaEntityType.METHOD)  
					|| change.getParentEntity().getType().equals(JavaEntityType.METHOD) 
					|| change.getChangedEntity().getType().equals(JavaEntityType.METHOD) )
			.forEach((methodChange) -> {
				changeMetric.updateMetricsPerChange(fileInfo, methodChange);
			});
		} else {
			Assert.fail();
		}
		Assert.assertTrue(changeMetric.getDecl()==1);
	}

	//TODO
	@Test
	public void methodNameChangeKeyTest() throws URISyntaxException{
		MethodBugLevelCollector methodBugLevelCollector = new MethodBugLevelCollector();
		FileInfoHelper fileInfo = methodBugLevelCollector.new FileInfoHelper();
		CommitInfov2 commitInfo = new CommitInfov2();
		fileInfo.setCommitInfo(commitInfo);
		ChangeMetric changeMetric = new ChangeMetric();
		
		String oldMethod="MethodChangeName.getAwesomeMethod()";
		String newMethodName="MethodChangeName.getAwesomeMethodMod()";
		int counter=0;

		URI uriV1 = this.getClass().getClassLoader().getResource("comparetest/v1/MethodChangeName.java").toURI();
		File left = Paths.get(uriV1).toFile();
		
		URI uriV2 = this.getClass().getClassLoader().getResource("comparetest/v2/MethodChangeName.java").toURI();
		File right = Paths.get(uriV2).toFile();
		
		FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
		try {
			distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch (Exception e) {
			System.err.println("Warning: error while change distilling. " + e.getMessage());
		}
		
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if (changes != null) {
			print(changes);
			changes.stream().filter((change) -> change.getRootEntity().getType().equals(JavaEntityType.METHOD)  
					|| change.getParentEntity().getType().equals(JavaEntityType.METHOD) 
					|| change.getChangedEntity().getType().equals(JavaEntityType.METHOD) )
			.forEach((methodChange) -> {
				if(methodChange.getChangeType().equals(ChangeType.METHOD_RENAMING)&&methodChange instanceof Update){
					 String newKey = ((Update)methodChange).getNewEntity().getUniqueName().toString();
					 String oldKey = ((Update)methodChange).getChangedEntity().getUniqueName().toString();
					 Assert.assertEquals(newKey, newMethodName);
					 Assert.assertEquals(oldKey, oldMethod);
					 changeMetric.updateMetricsPerChange(fileInfo, methodChange);
				}
			});
		} else {
			Assert.fail();
		}
		Assert.assertTrue(changeMetric.getDecl()==1);
		
	}
	
	//TODO
	@Test
	public void methodChangeParametersTest() throws URISyntaxException{
		MethodBugLevelCollector methodBugLevelCollector = new MethodBugLevelCollector();
		FileInfoHelper fileInfo = methodBugLevelCollector.new FileInfoHelper();
		CommitInfov2 commitInfo = new CommitInfov2();
		fileInfo.setCommitInfo(commitInfo);
		ChangeMetric changeMetric = new ChangeMetric();
		
		String oldMethod="MethodChangeName.getAwesomeMethod()";
		String newMethodName="MethodChangeName.getAwesomeMethodMod()";
		int counter=0;

		URI uriV1 = this.getClass().getClassLoader().getResource("comparetest/v1/MethodParameterChange.java").toURI();
		File left = Paths.get(uriV1).toFile();
		
		URI uriV2 = this.getClass().getClassLoader().getResource("comparetest/v2/MethodParameterChange.java").toURI();
		File right = Paths.get(uriV2).toFile();
		
		FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
		try {
			distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch (Exception e) {
			System.err.println("Warning: error while change distilling. " + e.getMessage());
		}
		
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if (changes != null) {
			print(changes);
			changes.stream().filter((change) -> change.getRootEntity().getType().equals(JavaEntityType.METHOD)  
					|| change.getParentEntity().getType().equals(JavaEntityType.METHOD) 
					|| change.getChangedEntity().getType().equals(JavaEntityType.METHOD))
			.forEach((methodChange) -> {
				if(methodChange.getChangeType().equals(ChangeType.METHOD_RENAMING)&&methodChange instanceof Update){
					 String newKey = ((Update)methodChange).getNewEntity().getUniqueName().toString();
					 String oldKey = ((Update)methodChange).getChangedEntity().getUniqueName().toString();
					 Assert.assertEquals(newKey, newMethodName);
					 Assert.assertEquals(oldKey, oldMethod);
					 changeMetric.updateMetricsPerChange(fileInfo, methodChange);
				}
			});
		} else {
			Assert.fail();
		}
		Assert.assertTrue(changeMetric.getDecl()==1);
	}
	
	//TODO
	@Test
	public void removeElseFromNestedLoopTwoLevelsTest(){}
	
	
	
	public void print(List<SourceCodeChange> changes){
		for( SourceCodeChange change :changes ){
			
			System.out.println("*");
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
			System.out.println("Changes Entity Label: " + change.getChangedEntity().getLabel());
			System.out.println("Changes Entity Unique Name: " + change.getChangedEntity().getUniqueName());
			System.out.println("Changes Entity Source Range: " + change.getChangedEntity().getSourceRange());
			System.out.println("Changes Entity Modifiers: " + change.getChangedEntity().getModifiers());
			System.out.println("Changes Entity Associated Entities: " + change.getChangedEntity().getAssociatedEntities());
			System.out.println("Changes Entity  Type: " + change.getChangedEntity().getType());
			System.out.println("Changes Type: " + change.getChangeType());
			System.out.println("Changes Label: " + change.getLabel());
			System.out.println("Changes Root Entity: " + change.getRootEntity());
			System.out.println("Changes Root Entity: " + change.getRootEntity().getVersion());
			
			System.out.println("****");
	        System.out.println("**");
	        System.out.println("*");
		}
	}
}