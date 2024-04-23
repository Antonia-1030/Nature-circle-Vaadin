package com.example.myproject.views.imagegallery;

import com.example.myproject.views.MainLayout;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

@PageTitle("Image Gallery")
@Route(value = "image-gallery", layout = MainLayout.class)
public class ImageGalleryView extends Main implements HasComponents, HasStyle {

    private OrderedList imageContainer;

    public ImageGalleryView() {
        constructUI();

        imageContainer.add(new ImageGalleryViewCard("GWC",
                "https://images-platform.99static.com/rJFFtzr16r47aGPw7-51x-WXiuw=/500x500/top/smart/99designs-contests-attachments/45/45507/attachment_45507391"));
        imageContainer.add(new ImageGalleryViewCard("sloths",
                "https://explorewildmadagascar.com/wp-content/uploads/2020/10/logo_opt.png"));
        imageContainer.add(new ImageGalleryViewCard("Rainforest",
                "https://th.bing.com/th/id/OIP.DaALsBiyBqHANW_029AKKQAAAA?w=300&h=300&rs=1&pid=ImgDetMain"));
        imageContainer.add(new ImageGalleryViewCard("wwf",
                "https://www.thoughtco.com/thmb/xeieMvGEOSvHEndmUASPlHwH5Kc=/1500x1000/filters:no_upscale():max_bytes(150000):strip_icc()/WWF_logo-58bd98d25f9b58af5cdd493e.jpg"));
        imageContainer.add(new ImageGalleryViewCard("ocean",
                "https://media.pagefly.io/file/get/oceana2jpg-1537640935019.jpg"));
        imageContainer.add(new ImageGalleryViewCard("fahlo",
                "https://th.bing.com/th/id/OIP.hSnBEVucqu_nNNTzICAd4wAAAA?rs=1&pid=ImgDetMain"));

    }

    private void constructUI() {
        addClassNames("image-gallery-view");
        addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Organizations");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Causes we support");
        description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        headerContainer.add(header, description);

        /*Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Popularity", "Newest first", "Oldest first");
        sortBy.setValue("Popularity");*/

        imageContainer = new OrderedList();
        imageContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE);

        container.add(headerContainer);
        add(container, imageContainer);

    }
}
