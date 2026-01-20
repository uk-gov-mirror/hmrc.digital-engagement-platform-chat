package uk.gov.hmrc.webchat.services

import com.google.inject.{ImplementedBy, Inject}
import play.api.Logging
import play.api.mvc.{ActionBuilder, ActionFunction, AnyContent, BodyParser, MessagesControllerComponents, Request, Result}
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions, Enrolments}
import uk.gov.hmrc.http.{HeaderCarrier, UnauthorizedException}
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import uk.gov.hmrc.webchat.models.AuthenticatedRequest

import scala.concurrent.{ExecutionContext, Future}

class AuthServiceImpl @Inject() (val authConnector: AuthConnector,
                                 mcc: MessagesControllerComponents)
                                (implicit val executionContext: ExecutionContext) extends AuthService with AuthorisedFunctions with Logging {

  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    authorised().retrieve(Retrievals.allEnrolments) {
      case Enrolments(enrolments) =>
        block(AuthenticatedRequest(request, enrolments))

      case _ => throw new UnauthorizedException("Unable to retrieve enrolments")
    }
  }

  override def parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser
}

@ImplementedBy(classOf[AuthServiceImpl])
trait AuthService extends ActionBuilder[AuthenticatedRequest, AnyContent] with ActionFunction[Request, Request]
