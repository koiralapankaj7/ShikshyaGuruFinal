package com.shikshyaguru.shikshyaguru._6_institutions_activity.views;
/*
 * Created by Pankaj Koirala on 9/30/2017.
 * Kathmandu, Nepal
 * Koiralapankaj007@gmail.com
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.shikshyaguru.shikshyaguru.R;
import com.shikshyaguru.shikshyaguru._0_6_widgets.Toolbars;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.model.InstitutionFakeDataSource;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.presenter.InstitutionsController;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerContactFragment;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerGalleryFragment;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerHomeFragment;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerManagementFragment;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerProgrammesFragment;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerReviewsFragment;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerStaffFragment;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerStudentsFragment;
import com.shikshyaguru.shikshyaguru._6_institutions_activity.views.viewpager_fragments.ViewPagerTeachersFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InstitutionsLoaderFragment extends Fragment {

    private SmartTabLayout viewPagerTab;
    private CollapsingToolbarLayout collapsingToolbar;
    private InstitutionsController controller;
    public static String id, image, name, place, slogan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout._6_2_0_ihp_inst_loader_fragment, container, false);

        if (getArguments() != null ) {
            id = getArguments().getString("ID");
            image = getArguments().getString("IMAGE");
            name = getArguments().getString("NAME");
            place = getArguments().getString("PLACE");
            slogan = getArguments().getString("SLOGAN");
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        Toolbars.setUpToolbar(toolbar, getActivity(), name);
        // To make onOptionItemSelected working we have to setHasOptionsMenu true in fragment.
        setHasOptionsMenu(true);

        controller = new InstitutionsController(new InstitutionFakeDataSource());

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);
        initSmartTabLayout(view);
        initFloatingActionMenu(view);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                Objects.requireNonNull(getActivity()).onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initComponents(View view) {

        collapsingToolbar = view.findViewById(R.id.ctbl_inst_loader_frag);

        KenBurnsView kenBurnsView = view.findViewById(R.id.iv_news_headline_icon);
        //TextView lblName = view.findViewById(R.id.lbl_institutions_name);
        TextView lblPlace = view.findViewById(R.id.lbl_institutions_city_name);
        TextView lblSlogan = view.findViewById(R.id.lbl_institutions_slogan);

        Picasso.get()
                .load(image)
                .placeholder(R.drawable.logo)
                .into(kenBurnsView);
        //lblName.setText(name);
        lblPlace.setText(place);

        if (slogan != null) {
            lblSlogan.setText(slogan);
        } else {
            System.out.println("======================================================");
            System.out.println(controller.getSlogan(id));
            lblSlogan.setText(controller.getSlogan(id));
        }

        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

    }

    private void initSmartTabLayout(View view) {

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getFragmentManager(), FragmentPagerItems.with(getContext())
                .add("Home", ViewPagerHomeFragment.class)
                .add("Programmes", ViewPagerProgrammesFragment.class)
                .add("Students", ViewPagerStudentsFragment.class)
                .add("Management", ViewPagerManagementFragment.class)
                .add("Gallery", ViewPagerGalleryFragment.class)
                .add("Teachers", ViewPagerTeachersFragment.class)
                .add("Staff's", ViewPagerStaffFragment.class)
                //.add("Activities", ViewPagerActivitiesFragment.class)
                .add("Contact", ViewPagerContactFragment.class)
                .add("Reviews", ViewPagerReviewsFragment.class)
                .create()
        );

        final ViewPager viewPager = view.findViewById(R.id.vp_inst_loader_frag);
        viewPager.setAdapter(adapter);

        viewPagerTab = view.findViewById(R.id.stl_inst_loader_frag);
        viewPagerTab.setViewPager(viewPager);
        viewPagerTab.setOnPageChangeListener(onPageChangeListener);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 2 || position == 3 || position == 4 || position == 5 || position == 6) {
                viewPagerTab.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.black_toolbar));
                collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.black_toolbar));
                collapsingToolbar.setStatusBarScrimColor(ContextCompat.getColor(getContext(), R.color.black_toolbar));
                //getActivity().getWindow().setBackgroundDrawableResource(R.color.card_black);
            } else {
                viewPagerTab.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAppMain));
                collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorAppMain));
                collapsingToolbar.setStatusBarScrimColor(ContextCompat.getColor(getContext(), R.color.colorAppMain));
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initFloatingActionMenu(View view) {
        FloatingActionMenu floatingActionMenu = view.findViewById(R.id.fam_inst_loader_frag);
        FloatingActionButton navigation = view.findViewById(R.id.fab_navigation_inst_loader_frag);
        FloatingActionButton facebook = view.findViewById(R.id.fab_facebook_inst_loader_frag);
        FloatingActionButton website = view.findViewById(R.id.fab_website_inst_loader_frag);
        FloatingActionButton share = view.findViewById(R.id.fab_share_inst_loader_frag);
        FloatingActionButton review = view.findViewById(R.id.fab_review_inst_loader_frag);
        FloatingActionButton favourite = view.findViewById(R.id.fab_favourite_inst_loader_frag);

        navigation.setImageResource(R.drawable.ic_navigation);
        facebook.setImageResource(R.drawable.ic_facebook_f);
        website.setImageResource(R.drawable.ic_website);
        share.setImageResource(R.drawable.ic_share_3);
        review.setImageResource(R.drawable.ic_review_message);
        favourite.setImageResource(R.drawable.ic_heart);

        navigation.setOnClickListener(floatingActionButtonClickListener);
        facebook.setOnClickListener(floatingActionButtonClickListener);
        website.setOnClickListener(floatingActionButtonClickListener);
        share.setOnClickListener(floatingActionButtonClickListener);
        review.setOnClickListener(floatingActionButtonClickListener);
        favourite.setOnClickListener(floatingActionButtonClickListener);

        ViewGroup.LayoutParams layoutParams = floatingActionMenu.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        floatingActionMenu.setLayoutParams(layoutParams);
        floatingActionMenu.setClosedOnTouchOutside(true);

    }

    private View.OnClickListener floatingActionButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_navigation_inst_loader_frag:
                    Toast.makeText(getContext(), "Navigation", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fab_facebook_inst_loader_frag:
                    Toast.makeText(getContext(), "Facebook", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fab_website_inst_loader_frag:
                    Toast.makeText(getContext(), "Website", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fab_share_inst_loader_frag:
                    Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fab_review_inst_loader_frag:
                    Toast.makeText(getContext(), "Review", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fab_favourite_inst_loader_frag:
                    Toast.makeText(getContext(), "Favourite", Toast.LENGTH_SHORT).show();








                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

                    //final HashMap<String, ArrayList<String>> categoriesWithImage = new HashMap<>();
                    //InstitutionGalleryData galleryData = new  InstitutionGalleryData();

                    Query query = mDatabase.getReference().child("clients").child("client-02").child("app_gallery");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // Array which holds list of images for single category
                            //ArrayList<String> images = new ArrayList<>();

                            HashMap<String, ArrayList<String>> categoryWithImages = new HashMap<>();
                            HashMap<String, ArrayList<String>> categoryWithDescription = new HashMap<>();
                            HashMap<String, ArrayList<String>> categoryWithIds = new HashMap<>();

                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {

                                ArrayList<String> images = new ArrayList<>();
                                ArrayList<String> description = new ArrayList<>();
                                ArrayList<String> ids = new ArrayList<>();

                                for (DataSnapshot imagesSnapshot : categorySnapshot.getChildren()) {

                                    String image = imagesSnapshot.child("image_url").getValue(String.class);
                                    String desc = imagesSnapshot.child("image_description").getValue(String.class);
                                    String time = imagesSnapshot.getKey();

                                    images.add(image);
                                    description.add(desc);
                                    ids.add(time);

                                    System.out.println("============================"+categorySnapshot.getChildrenCount()+"========================");
                                    System.out.println(time);
                                    System.out.println("============================"+categorySnapshot.getKey()+"========================");

                                }

                                categoryWithImages.put(categorySnapshot.getKey(), images);
                                categoryWithDescription.put(categorySnapshot.getKey(), description);
                                categoryWithIds.put(categorySnapshot.getKey(), ids);

                            }

                            System.out.println("============================"+categoryWithImages.size()+"========================");


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
























                    break;
                default:
                    break;
            }
        }
    };


}
