package Iscte.GrupoR.testing;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ OpenFileCSVTest.class,GenerateHTMLTest.class,CompareCSVFilesTest.class, MetricasTest.class})
public class AllTests {

}
