package com.example.myproject.views.naturecircle;

import com.example.myproject.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Nature Circle")
@Route(value = "homepage", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class NatureCircleView extends Composite<VerticalLayout> {

    public NatureCircleView() {
        H1 h1 = new H1();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Paragraph textLarge = new Paragraph();
        FormLayout formLayout3Col = new FormLayout();
        Paragraph textMedium = new Paragraph();
        Paragraph textMedium2 = new Paragraph();
        Paragraph textMedium3 = new Paragraph();
        Button buttonPrimary = new Button();
        HorizontalLayout layoutRow = new HorizontalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h1.setText("Nature Circle");
        h1.setWidth("max-content");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        textLarge.setText("Helping nature is our thing. Come Join us!");
        textLarge.setWidth("100%");
        textLarge.getStyle().set("font-size", "var(--lumo-font-size-xl)");
        formLayout3Col.setWidth("100%");
        formLayout3Col.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("250px", 2),
                new ResponsiveStep("500px", 3));
        textMedium.setText(
                "The Earth is our home and we must protect it!");
        textMedium.setWidth("100%");
        textMedium.getStyle().set("font-size", "var(--lumo-font-size-m)");
        textMedium2.setText(
                "We at Nature circle believe change comes with small but many steps. By buying our products you help save nature.");
        textMedium2.setWidth("100%");
        textMedium2.getStyle().set("font-size", "var(--lumo-font-size-m)");
        textMedium3.setText(
                "We fund many wildlife and nature-preserving companies by giving half our profits to support their cause.");
        textMedium3.setWidth("100%");
        textMedium3.getStyle().set("font-size", "var(--lumo-font-size-m)");
        buttonPrimary.setText("Join us");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        getContent().add(h1);
        getContent().add(layoutColumn2);
        layoutColumn2.add(textLarge);
        layoutColumn2.add(formLayout3Col);
        formLayout3Col.add(textMedium);
        formLayout3Col.add(textMedium2);
        formLayout3Col.add(textMedium3);
        layoutColumn2.add(buttonPrimary);
        getContent().add(layoutRow);
    }
}
