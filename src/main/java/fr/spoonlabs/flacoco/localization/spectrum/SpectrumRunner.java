package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.api.result.FlacocoResult;
import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.CoverageMatrix;
import fr.spoonlabs.flacoco.core.coverage.CoverageRunner;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestDetector;
import fr.spoonlabs.flacoco.localization.FaultLocalizationRunner;
import fr.spoonlabs.flacoco.utils.spoon.SpoonConverter;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class SpectrumRunner implements FaultLocalizationRunner {

    private Logger logger = Logger.getLogger(SpectrumRunner.class);

    private FlacocoConfig config;

    public SpectrumRunner(FlacocoConfig config) {
        this.config = config;
    }

    @Override
    public FlacocoResult run() {
        FlacocoResult result = new FlacocoResult();

        CoverageMatrix coverageMatrix = computeCoverageMatrix();
        result.setFailingTests(coverageMatrix.getFailingTestCases());

        SpectrumSuspiciousComputation ssc = new SpectrumSuspiciousComputation(config);
        Map<Location, Suspiciousness> defaultMapping = ssc.calculateSuspicious(coverageMatrix, this.config.getSpectrumFormula().getFormula());
        result.setDefaultSuspiciousnessMap(defaultMapping);

        if (config.isComputeSpoonResults()) {
            result = new SpoonConverter(config).convertResult(result);
        }

        return result;
    }

    private CoverageMatrix computeCoverageMatrix() {
        this.logger.debug("Running spectrum-based fault localization...");
        this.logger.debug(this.config);

        // Get the tests
        TestDetector testDetector = new TestDetector(config);
        List<TestContext> tests = testDetector.getTests();

        CoverageRunner detector = new CoverageRunner(config);

        return detector.getCoverageMatrix(tests);
    }
}