package uk.gov.hmrc.webchat.models

import play.api.mvc.{Request, WrappedRequest}
import uk.gov.hmrc.auth.core.Enrolment


case class AuthenticatedRequest[A] (
                                     request: Request[A],
                                   enrolments: Set[Enrolment]
                                   ) extends WrappedRequest[A](request)
