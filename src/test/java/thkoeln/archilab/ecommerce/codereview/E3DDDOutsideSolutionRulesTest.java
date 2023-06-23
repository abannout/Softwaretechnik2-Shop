package thkoeln.archilab.ecommerce.codereview;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@ArchTag("layerRules")
@AnalyzeClasses(packages = "thkoeln")
@SuppressWarnings("PMD")
public class E3DDDOutsideSolutionRulesTest {

    @ArchTest
    static final ArchRule noClassesOutsideSolutionAndDomainprimitives =
            layeredArchitecture()
                    .consideringAllDependencies()
                    .layer( "Domainprimitives" ).definedBy( "thkoeln.archilab.ecommerce.domainprimitives.." )
                    .layer( "SolutionClasses" ).definedBy( "thkoeln.archilab.ecommerce.solution.." )
                    .layer( "Test1" ).definedBy( "thkoeln.archilab.ecommerce.codereview.." )
                    .layer( "Test2" ).definedBy( "thkoeln.archilab.ecommerce.e1e2resttests.." )
                    .layer( "Test3" ).definedBy( "thkoeln.archilab.ecommerce.regression.." )

                    .whereLayer( "Domainprimitives" ).mayOnlyBeAccessedByLayers( "SolutionClasses", "Test1", "Test2", "Test3" )
                    .whereLayer( "SolutionClasses" ).mayOnlyBeAccessedByLayers( "Test1", "Test2", "Test3" );
}
