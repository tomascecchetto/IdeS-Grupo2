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

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
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
//menu contextual lista de canciones
public class test_menu_lista_canciones {
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
    //hace clic sobre una cancion de la lista y chequea que se despliegue el menu contextual
    //se espera mque se despliegue el menu en pantalla
    public void test_despliegue_menu() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(childAtPosition(withId(R.id.drawer_layout), 0), 2), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista), childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(3, longClick()));

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("Abrir"),
                        childAtPosition(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class), childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), 0)), 0), isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(android.R.id.text1), withText("Añadir a Favoritos"),
                        childAtPosition(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class), childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), 0)), 1), isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(android.R.id.text1), withText("Copiar a Carpeta"),
                        childAtPosition(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class), childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), 0)), 2), isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(android.R.id.text1), withText("Mover a Carpeta"),
                        childAtPosition(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class), childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), 0)), 3), isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(android.R.id.text1), withText("Renombrar canción"),
                        childAtPosition(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class), childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), 0)), 4), isDisplayed()));
        textView6.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(android.R.id.text1), withText("Eliminar canción"),
                        childAtPosition(allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class), childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), 0)), 5), isDisplayed()));
        textView7.check(matches(isDisplayed()));

    }

    @Test
    //hace clic sobre una cancion de la lista y chequea que se despliegue el menu contextual
    //hace clic sobre la opcion abrir
    //se espera que muestre el detalle de la cancion
    public void test_menu_abrir(){
        ViewInteraction appCompatImageButton2 = onView(allOf(withContentDescription("Open navigation drawer"),
                childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1), isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 1), isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista), childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0))).atPosition(0);
        appCompatTextView.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.cancion_detalle_secciones), withParent(withParent(withId(R.id.cancionmusico_detail_container))), isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));
    }

    @Test
    //hace clic sobre una canciion de la lista y chequea que se despliegue el menu contextual
    //hace clic sobre la opcion agregar a favoritos
    //muestra la pantalla principal con la lista de favoritos
    //se espera que la cancion agregada este en la lista
    public void test_menu_favoritos(){
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1), isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 1), isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista), childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)))
                .atPosition(1);
        appCompatTextView.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar), 0)), 1), isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.favoritos_lista_item_titulo), withText("Canción Dummy 0"),
                        withParent(withParent(withId(R.id.favoritos_lista))), isDisplayed()));
        textView.check(matches(withText("Canción Dummy 0")));

    }

    @Test
    //agrega uan cancion a favoritos
    //hace clic en la lista de favoritos
    //se spera que se despliegue una confirmacion de eliminar la cancion de favoritos
    public void test_menu_eliminar_de_favoritos(){
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(allOf(withId(R.id.design_navigation_view),
                        childAtPosition(withId(R.id.nav_view), 0)), 1),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)))
                .atPosition(1);
        appCompatTextView.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar), 0)), 1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.favoritos_lista),
                        childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 1)));
        recyclerView2.perform(actionOnItemAtPosition(0, longClick()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button2), withText("Cancelar"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 2)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.favoritos_lista),
                        childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 1)));
        recyclerView3.perform(actionOnItemAtPosition(0, longClick()));

        ViewInteraction textView = onView(
                allOf(IsInstanceOf.<View>instanceOf(android.widget.TextView.class), withText("Remover Canción Dummy 0 de la lista de favoritos"),
                        withParent(allOf(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView.check(matches(withText("Remover Canción Dummy 0 de la lista de favoritos")));
    }

    @Test
    //hace clic sobre una canciion de la lista y chequea que se despliegue el menu contextual
    //se espera mque se despliegue el menu en pantalla
    public void test_menu_copiar(){
        /*TODO*/
    }

    @Test
    //hace clic sobre una cancion de la lista y chequea que se despliegue el menu contextual
    //se espera mque se despliegue el menu en pantalla
    public void test_menu_mover(){
/*TODO*/
    }

    @Test
    //hace clic sobre una canciion de la lista y chequea que se despliegue el menu contextual
    //hace click sobre la opcion renombrar
    //renombra la cancion y hace click en aceptar
    //se espera que la cancion este en la lista con el nuevo nombre
    public void test_menu_renombrar(){
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)),1),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)))
                .atPosition(4);
        appCompatTextView.perform(click());

        ViewInteraction editText = onView(
                allOf(withText("Canción Dummy 0"),
                        childAtPosition(allOf(withId(android.R.id.custom), childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)), 0),
                        isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withText("Canción Dummy 0"),
                        childAtPosition(allOf(withId(android.R.id.custom), childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)), 0),
                        isDisplayed()));
        editText2.perform(replaceText("Canción Dummy renombrada"));

        ViewInteraction editText3 = onView(
                allOf(withText("Canción Dummy renombrada"),
                        childAtPosition(allOf(withId(android.R.id.custom), childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)), 0),
                        isDisplayed()));
        editText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy renombrada"), withParent(withParent(withId(R.id.cancion_lista))),
                        isDisplayed()));
        textView.check(matches(withText("Canción Dummy renombrada")));
    }

    @Test
    //hace clic sobre una canciion de la lista y chequea que se despliegue el menu contextual
    //hace click sobre la opcion eliminar
    //se espera que la cancion no este mas en la lista
    public void test_menu_eliminar(){
        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("android.support.design.widget.AppBarLayout")), 0)), 1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction navigationMenuItemView3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 1),
                        isDisplayed()));
        navigationMenuItemView3.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")), 0)))
                .atPosition(5);
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 0"),
                        withParent(withParent(withId(R.id.cancion_lista))),
                        isDisplayed()));
        textView.check(doesNotExist());
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
