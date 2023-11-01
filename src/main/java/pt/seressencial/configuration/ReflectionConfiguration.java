package pt.seressencial.configuration;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.services.pagespeedonline.v5.PagespeedInsights;
import com.google.api.services.pagespeedonline.v5.model.Bucket;
import com.google.api.services.pagespeedonline.v5.model.LighthouseAuditResultV5;
import com.google.api.services.pagespeedonline.v5.model.LighthouseResultV5;
import com.google.api.services.pagespeedonline.v5.model.PagespeedApiPagespeedResponseV5;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets={ GoogleJsonError.class, GoogleJsonError.ErrorInfo.class, GoogleJsonError.Details.class,
        GoogleJsonError.ParameterViolations.class, PagespeedInsights.Pagespeedapi.Runpagespeed.class, PagespeedApiPagespeedResponseV5.class,
        LighthouseResultV5.class, Bucket.class, LighthouseAuditResultV5.class, Object.class},
        registerFullHierarchy = true)
public class ReflectionConfiguration {
}