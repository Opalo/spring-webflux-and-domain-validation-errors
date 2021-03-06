package org.opal;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReqTracer {

  private final Tracer tracer;

  String traceId() {
    return tracer.currentSpan().context().traceIdString();
  }
  
}
