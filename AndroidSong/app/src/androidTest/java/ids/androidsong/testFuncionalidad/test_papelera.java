package ids.androidsong.testFuncionalidad;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewAssertion;
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
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.cancionesDummy;
import ids.androidsong.help.App;
import ids.androidsong.object.Cancion;
import ids.androidsong.ui.Principal;

@LargeTest
@RunWith(AndroidJUnit4.class)

public class test_papelera {
    private static  int CANTIDAD_CANCIONES_DUMMY = 2;
    private static int CANTIDAD_SECCIONES_DUMMY = 3;

    @Rule
    public ActivityTestRule<Principal> mActivityTestRule = new ActivityTestRule<>(Principal.class);

    @Before
    public void setup(){
        /*Pide canciones dummy y las inserta*/
        ArrayList<Cancion> canciones = new ArrayList<>();
        cancionesDummy cancionesDummy = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY);
        canciones.addAll(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY));
        canciones.addAll(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY,"Pruebas"));
        for (Cancion c : canciones){
            c.alta();
        }
    }

    @Test
    //inserta una serie de canciones y elimina una de ellas
    //abre la papelera y hace click sobre el boton restaurar
    //se espera que al abrir la lista de canciones la cancion restaurada se encuentre en la lista
    public void test_papelera_restaurar() {
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 1),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)))
                .atPosition(5);
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar), 0)), 1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction navigationMenuItemView3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 3),
                        isDisplayed()));
        navigationMenuItemView3.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.papelera_lista_item_restaurar),
                        childAtPosition(childAtPosition(withId(R.id.papelera_lista), 0), 1),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton6.perform(click());

        ViewInteraction appCompatImageButton7 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton7.perform(click());

        ViewInteraction navigationMenuItemView4 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 1),
                        isDisplayed()));
        navigationMenuItemView4.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 0"), withParent(withParent(withId(R.id.cancion_lista))),
                        isDisplayed()));
        textView.check(matches(withText("Canción Dummy 0")));

    }

    @Test
    //inserta una serie de canciones y elimina una de ellas
    //abre la papelera y hace click sobre el boton eliminar
    //se espera que la cancion sea eliminada de la papelera
    public void test_papelera_eliminar() {
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 1),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)))
                .atPosition(5);
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar), 0)), 1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction navigationMenuItemView3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 3),
                        isDisplayed()));
        navigationMenuItemView3.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.papelera_lista_item_borrar),
                        childAtPosition(childAtPosition(withId(R.id.papelera_lista), 0), 2),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(IsInstanceOf.<View>instanceOf(android.widget.TextView.class), withText("¿Eliminar \"Canción Dummy 0\" de forma permanente?"),
                        withParent(allOf(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView.check(matches(withText("¿Eliminar \"Canción Dummy 0\" de forma permanente?")));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.papelera_lista), withParent(withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class))),
                        isDisplayed()));
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
