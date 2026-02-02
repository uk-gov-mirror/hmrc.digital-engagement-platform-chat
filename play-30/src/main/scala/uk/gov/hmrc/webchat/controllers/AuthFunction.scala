package uk.gov.hmrc.webchat.controllers

import play.api.Logging
import play.api.mvc.{Request}
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals

import scala.concurrent.{ExecutionContext, Future}
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.webchat.models.UserProfile

import javax.inject.Inject;

class AuthFunction @Inject() (override val authConnector: AuthConnector)
                             (implicit executionContext: ExecutionContext) extends AuthorisedFunctions with Logging {

   def getEnrolments(implicit request: Request[_], hc: HeaderCarrier): Future[UserProfile] = {
    logger.debug("[AuthFunction][getEnrolments] retrieve enrolments")

    authorised()
      .retrieve(Retrievals.allEnrolments) {
        case allEnrolments =>
          Future.successful(UserProfile(allEnrolments))
      }
  }
}