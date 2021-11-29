package ids.androidsong.testValidacion;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ids.androidsong.R;
import ids.androidsong.help.App;
import ids.androidsong.ui.Principal;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class test_validacion_campos {

    @Rule
    public ActivityTestRule<Principal> mActivityTestRule = new ActivityTestRule<>(Principal.class);

    @Test
    //inserta un tono para la cancion fuera del rango permitido
    //C C# D D# E F F# G G# A A# B
    //se espera un mensaje de error indicando que el valor ingresado esta fuera de rango
    public void test_validacion_tonalidad_1() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget" + ".AppBarLayout")), 0)), 1), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 2), isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.nueva_cancion_tono)));
        appCompatEditText.perform(click());
        appCompatEditText.perform(scrollTo(), replaceText("Z"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.nueva_cancion_presentacion)));
        appCompatEditText2.perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("El valor está fuera del rango admitido: A a b")))
                .check(matches(isDisplayed()));
    }

    @Test
    //inserta un entero en el campo tonalidad
    //C C# D D# E F F# G G# A A# B
    //se espera un mensaje de error indicando que el valor ingresado esta fuera de rango
    public void test_validacion_tonalidad_2() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget" + ".AppBarLayout")), 0)), 1), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 2), isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.nueva_cancion_tono)));
        appCompatEditText.perform(click());
        appCompatEditText.perform(scrollTo(), replaceText("4"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.nueva_cancion_presentacion)));
        appCompatEditText2.perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("El valor está fuera del rango admitido: A a b")))
                .check(matches(isDisplayed()));
    }

    @Test
    //inserta un numero de transporte que esta fuera del rango permitido
    //se espera un mensaje de error
    public void test_validacion_transporte_1() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.nueva_cancion_transporte)));
        appCompatEditText.perform(scrollTo(), replaceText("13"), closeSoftKeyboard());
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.nueva_cancion_titulo)));
        appCompatEditText2.perform(scrollTo(),click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("El valor está fuera del rango admitido: 1 a 12")))
                .check(matches(isDisplayed()));
    }

    @Test
    //inserta un string en el numero de transporte
    //se espera un mensaje de error
    public void test_validacion_transporte_2() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.nueva_cancion_transporte)));
        appCompatEditText.perform(scrollTo(), replaceText("abc"), closeSoftKeyboard());
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.nueva_cancion_titulo)));
        appCompatEditText2.perform(scrollTo(),click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("El valor no es numérico: abc")))
                .check(matches(isDisplayed()));
    }

    @Test
    //inserta un nombre de carpeta que excede la cantidad de caracteres permitidos
    //se espera un mensaje de error
    public void test_validacion_carpeta() {
        ViewInteraction appCompatImageButton = onView(allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget" + ".AppBarLayout")), 0)), 1), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 2), isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatImageButton2 = onView(allOf(withId(R.id.imageButton), childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")), 1), 1)));
        appCompatImageButton2.perform(scrollTo(), click());

        ViewInteraction editText = onView(allOf(childAtPosition(allOf(withId(android.R.id.custom), childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)), 0), isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(allOf(childAtPosition(allOf(withId(android.R.id.custom), childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)), 0), isDisplayed()));
        editText2.perform(replaceText("1234567890poiuytrewqasdfghjklñpoiuytrewq123456789012"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(allOf(withId(android.R.id.button1), withText("Aceptar"), childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction textView = onView(allOf(withId(android.R.id.message), withText("Cantidad máxima de caracteres (50) alcanzada"), isDisplayed()));
        textView.check(matches(isDisplayed()));

    }

    @Test
    //inserta un nombre de cancion que excede la cantidad de caracteres permitidos
    //se espera un mensaje de error
    public void test_validacion_nombre() {
        ViewInteraction appCompatImageButton3 = onView(allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget" + ".AppBarLayout")),0)), 1), isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction navigationMenuItemView = onView(allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view),0)), 2), isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.nueva_cancion_titulo)));
        appCompatEditText2.perform(click());
        appCompatEditText2.perform(scrollTo(), replaceText("1234567890poiuytrewqasdfghjklñpoiuytrewq123456789012"), closeSoftKeyboard());

        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.nueva_cancion_presentacion)));
        appCompatEditText.perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Cantidad máxima de caracteres (50) alcanzada"))).check(matches(isDisplayed()));

    }

    @Test
    //inserta un string con el autor de la cancion que excede la cantidad de caracteres permitidos
    //se espera un mensaje de error
    public void test_validacion_autor() {
        ViewInteraction appCompatImageButton = onView(allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget" + ".AppBarLayout")), 0)), 1), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 2), isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.nueva_cancion_autor), childAtPosition(childAtPosition(withId(R.id.scrollView2), 0), 5)));
        appCompatEditText.perform(click());

        appCompatEditText.perform(scrollTo(), replaceText("1234567890poiuytrewqasdfghjklñpoiuytrewq123456789012"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.nueva_cancion_titulo)));
        appCompatEditText2.perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Cantidad máxima de caracteres (50) alcanzada"))).check(matches(isDisplayed()));
    }

    @Test
    //inserta un nombre de presentacion (???) que excede la cantidad de caracteres permitidos
    //se espera un mensaje de error
    public void test_validacion_presentacion() {
        ViewInteraction appCompatImageButton = onView(allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget" + ".AppBarLayout")), 0)), 1), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 2), isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.nueva_cancion_presentacion)));
        appCompatEditText.perform(click());

        appCompatEditText.perform(scrollTo(), replaceText("1234567890poiuytrewqasdfghjklñpoiuytrewq123456789012"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.nueva_cancion_titulo)));appCompatEditText2.perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Cantidad máxima de caracteres (50) alcanzada"))).check(matches(isDisplayed()));
    }

    @After
    public void destroy(){
        try {
            App.GetDBHelper().clearDb();
            App.CloseDB();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
