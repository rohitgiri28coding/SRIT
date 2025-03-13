package com.coaching.srit.ui.screens.auth.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.coaching.srit.R
import com.coaching.srit.domain.UserErrorEvent
import com.coaching.srit.ui.ObserveEvents
import com.coaching.srit.ui.components.BackgroundImage
import com.coaching.srit.ui.components.ButtonComponent
import com.coaching.srit.ui.components.ClickableLoginTextComponent
import com.coaching.srit.ui.components.GoogleSignInButton
import com.coaching.srit.ui.components.HeadingTextComposable
import com.coaching.srit.ui.components.MyPasswordTextField
import com.coaching.srit.ui.components.MyTextField
import com.coaching.srit.ui.components.NormalTextComposable
import com.coaching.srit.ui.components.Spacing
import com.coaching.srit.ui.components.UnderlinedTextComposable
import com.coaching.srit.ui.navigation.Router
import com.coaching.srit.ui.navigation.Screen
import com.coaching.srit.ui.navigation.SystemBackButtonHandler
import com.coaching.srit.ui.theme.Primary
import com.coaching.srit.ui.viewmodel.AuthUiEvent
import com.coaching.srit.ui.viewmodel.SignInViewModel

@Composable
fun LoginScreen(signInViewModel: SignInViewModel = hiltViewModel()) {

    val context = LocalContext.current
    ObserveEvents(flow = signInViewModel.signInErrorEvents) {
        when(it){
            is UserErrorEvent.Error -> {
                Toast.makeText(context, "Sign In Error: ${it.error.asString(context)}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    Box {
        BackgroundImage()
        Column(
            modifier = Modifier
                .padding(start = 20.dp, top = 80.dp, end = 20.dp)
        ) {
            HeadingTextComposable(
                textValue = stringResource(R.string.hey_there)
            )
            Spacing(size = 15.dp)
            NormalTextComposable(
                textValue = stringResource(R.string.welcome_msg),
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
            )
            Spacing(size = 40.dp)
            MyTextField(
                labelValue = stringResource(R.string.email),
                painterResource = painterResource(id = R.drawable.email),
                onTextSelected = {
                    signInViewModel.onEvent(AuthUiEvent.EmailChange(it))
                }
            )
            Spacing(size = 10.dp)
            MyPasswordTextField(
                labelValue = stringResource(R.string.password),
                painterResource = painterResource(id = R.drawable.password),
                onTextSelected = {
                    signInViewModel.onEvent(AuthUiEvent.PasswordChange(it))
                }
            )
            Spacing(size = 20.dp)
            UnderlinedTextComposable(textValue = stringResource(R.string.forgot_your_password)) {
                Router.navigateTo(Screen.ForgotPasswordScreen)
            }
            Spacing(size = 20.dp)
            ButtonComponent(value = stringResource(id = R.string.login))
            {
                signInViewModel.onEvent(AuthUiEvent.AuthButtonClicked)
            }
            GoogleSignInButton{
                signInViewModel.onEvent(AuthUiEvent.GoogleAuthButtonClicked(context))
            }
            Spacer(modifier = Modifier.weight(1f))
            ClickableLoginTextComponent(
                text = stringResource(R.string.don_t_have_an_account),
                clickableText = stringResource(R.string.register)
            ) {
                Router.navigateTo(Screen.SignUpScreen)
            }
            Spacing()
        }
        SystemBackButtonHandler {
            Router.navigateTo(Screen.WelcomeScreen)
        }
        if (signInViewModel.signInInProgress.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Primary
            )
        }
    }
}