package controllers

import models.User
import utils.silhouette._
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import com.mohiva.play.silhouette.api.{Environment, LoginInfo}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.mohiva.play.silhouette.test._
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.concurrent.Execution.Implicits._
import play.api.test.{FakeRequest, PlaySpecification, WithApplication}

/**
 * Test case for the [[controllers.Application]] class.
 */
class ApplicationSpec extends PlaySpecification with Mockito {
  sequential

  "The `index` action" should {
    "redirect to login page if user is unauthorized" in new Context {
      new WithApplication(application) {
        val Some(redirectResult) = route(app, FakeRequest(routes.Application.myAccount)
          .withAuthenticator[MyEnv](LoginInfo("invalid", "invalid")))

        status(redirectResult) must be equalTo SEE_OTHER

        val redirectURL = redirectLocation(redirectResult).getOrElse("")
        redirectURL must contain(routes.Auth.signIn.toString)

        val Some(unauthorizedResult) = route(app, FakeRequest(GET, redirectURL))

        status(unauthorizedResult) must be equalTo OK
        contentType(unauthorizedResult) must beSome("text/html")
        contentAsString(unauthorizedResult) must contain("Sign In")
      }
    }

    "return 200 if user is authorized" in new Context {
      new WithApplication(application) {
        val Some(result) = route(app, FakeRequest(routes.Application.myAccount)
          .withAuthenticator[MyEnv](identity.loginInfo))

        status(result) must beEqualTo(OK)
      }
    }
  }

  /**
   * The context.
   */
  trait Context extends Scope {

    class FakeModule extends AbstractModule with ScalaModule {
      def configure() = {
        bind[Environment[MyEnv]].toInstance(env)
      }
    }

    val users = scala.collection.mutable.HashMap[Long, User](
      1L -> User(Some(1L), "master@myweb.com", true, (new BCryptPasswordHasher()).hash("123123").password, "Eddy", "Eddard", "Stark", List("master")),
      2L -> User(Some(2L), "a@myweb.com", true, (new BCryptPasswordHasher()).hash("123123").password, "Maggy", "Margaery", "Tyrell", List("serviceA")),
      3L -> User(Some(3L), "b@myweb.com", true, (new BCryptPasswordHasher()).hash("123123").password, "Petyr", "Petyr", "Baelish", List("serviceB")),
      4L -> User(Some(4L), "a_b@myweb.com", true, (new BCryptPasswordHasher()).hash("123123").password, "Tyry", "Tyrion", "Lannister", List("serviceA", "serviceB"))
    )

    val identity = users.find(_._2.email == "a@myweb.com").map(_._2).get

    implicit val env: Environment[MyEnv] = new FakeEnvironment[MyEnv](Seq(identity.loginInfo -> identity))

    lazy val application = new GuiceApplicationBuilder().overrides(new FakeModule()).build
  }
}
