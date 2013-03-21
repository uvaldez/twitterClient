/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uzielvaldez.twitterapi.basic;

/**
 *
 * @author uzielvaldez
 */



import java.awt.Color;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import twitter4j.*;

public class main extends javax.swing.JFrame {

    //Global Variables
    Twitter twitter = TwitterFactory.getSingleton();
    ImageIcon imgTimeline = new ImageIcon("images/timeline.png");
    ImageIcon imgMentions = new ImageIcon("images/mentions.png");
    ImageIcon imgDirectMjs = new ImageIcon("images/directMessages.png");
    ImageIcon imgSearch = new ImageIcon("images/search.png");
    ImageIcon imgTweet = new ImageIcon("images/tweet.png");
    ImageIcon imgTwLogo = new ImageIcon("images/twlogo2.jpg");
    ImageIcon imgTimelineSelected = new ImageIcon("images/timelineSelected.png");
    ImageIcon imgMentionsSelected = new ImageIcon("images/mentionsSelected.png");
    ImageIcon imgDirectMjsSelected = new ImageIcon("images/directMessagesSelected.png");
    ImageIcon imgSearchSelected = new ImageIcon("images/searchSelected.png");    
    public main() {
        initComponents();        
        initApp();
        //getTrends();
    }

    public void initApp(){
        configurateButtons();
        txtSearch.setVisible(false);
        tweetContainer.setVisible(false);
        fillTimeLine("timeline");
        getContentPane().setBackground(Color.black);
        setSize(700, 650);
        setLocationRelativeTo(null);
    }
    public void configurateButtons()
    {
        btnTimeline.setSize(70, 50);
        btnMentions.setSize(70, 50);
        btnDirectMjs.setSize(70, 50);
        btnSearch.setSize(70, 50);
        btnTweet.setSize(60, 40);
        setDeselected();
        btnTimeline.setIcon(imgTimelineSelected);
        btnTweet.setIcon(imgTweet);
        twLogo.setIcon(imgTwLogo);
        Titulo.setForeground(Color.white);
        try{
            long user_id = twitter.getId();
            int followers = twitter.showUser(user_id).getFollowersCount();
            int following = twitter.showUser(user_id).getFriendsCount();
            int tweets = twitter.showUser(user_id).getStatusesCount();
            int favs = twitter.showUser(user_id).getFavouritesCount();
            lbFollowers.setText(followers+"");
            lbFollowing.setText(following+"");
            lbTweets.setText(tweets+"");
            lbFavorites.setText(favs+"");            
            Trends trends = twitter.getPlaceTrends(1);
            System.out.println(trends.getTrends());
            Trends.setContentType("text/html");
            String txtTrends = "";
            for (Trend trend : trends.getTrends()) {
                txtTrends = txtTrends+" <p style='color:#1D5F8C'>"+trend.getName()+"</p>";
                System.out.println(" " + trend.getName());
            }
            Trends.setBackground(Color.black);
            Trends.setText(txtTrends);
        }
        catch(TwitterException ex)
        {
            
        }        
    }
    
    public void setDeselected()
    {
        btnTimeline.setIcon(imgTimeline);
        btnMentions.setIcon(imgMentions);
        btnDirectMjs.setIcon(imgDirectMjs);
        btnSearch.setIcon(imgSearch);
        btnTweet.setIcon(imgTweet);
        twLogo.setIcon(imgTwLogo);
    }
    
    public void fillTimeLine(String type)
    {
        EditorTimeline.setContentType("text/html");
        String texto="";
        List <Status> statuses;
        try {
            if(type.toLowerCase().equals("timeline")){
                statuses = twitter.getHomeTimeline();}
            else {statuses = twitter.getMentionsTimeline();}             
            
        for (Status status : statuses) {
            String statusText = status.getText();

            StringTokenizer stTokens = new StringTokenizer(statusText);
            String TextFormated = "";
            while (stTokens.hasMoreTokens()) {
                    String token = stTokens.nextToken();
                    if(token.contains("#")){
                        TextFormated = TextFormated +" <a href='#' style='font-weight:bold;color:#1D5F8C'>"+token+"</a> ";}
                    else if(token.contains("http")){
                        TextFormated = TextFormated +" <a href='"+token+"' style='font-weight:bold;color:#1D5F8C'>"+token+"</a> ";}
                    else{
                        TextFormated = TextFormated +" "+ token;}
                }
            texto = texto + "<div style='margin-top:8px;float:left;height:55px;width:300px;border-bottom:1px solid #bbbab7;padding-bottom:3px'>";
            texto = texto + "<img src='"+status.getUser().getOriginalProfileImageURL()+"' width='55px' height='53px' style='display:inline-block;margin-right:3px'><p style='display:inline-block;margin-left:3px'><b>"+ status.getUser().getName() + "</b>" + ":" + TextFormated +"</p>";            
            texto = texto + "</div>";
            //texto = texto + "<hr>";
            }
        }        
        catch (TwitterException ex) {
            JOptionPane.showMessageDialog(this, ex);            
        }
        EditorTimeline.setText(texto);
        EditorTimeline.setEditable(false);        
    }

    public void getTrends()
    {
        try{
    //List<String> currentTrends = new ArrayList<String>();
            Trends trends = twitter.getLocationTrends(1);
    for(Trend t: trends.getTrends()){
        String name = t.getName();
        System.out.println(name);
        //currentTrends.add(name);
    }
        List <Location> locations;
        locations = twitter.getAvailableTrends();
        for(Location location:locations)
        {
            Trends t = twitter.getPlaceTrends(location.getWoeid());
            Trend tmp[] = t.getTrends();

            for(int i=0;i<10;i++)
            {
                System.out.println(tmp[i]);
            }
            System.out.println(location.getWoeid());
        }
       }
        catch(TwitterException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    public void SearchTweets(String searchText)
    {
        String texto="";
        try{        
        Query query = new Query(searchText);
        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            String statusText = status.getText();

            StringTokenizer stTokens = new StringTokenizer(statusText);
            String TextFormated = "";
            while (stTokens.hasMoreTokens()) {
                    String token = stTokens.nextToken();
                    if(token.contains("#")){
                        TextFormated = TextFormated +" <a href='#' style='font-weight:bold;color:#1D5F8C'>"+token+"</a> ";}
                    else if(token.contains("http")){
                        TextFormated = TextFormated +" <a href='"+token+"' style='font-weight:bold;color:#1D5F8C'>"+token+"</a> ";}
                    else if(token.contains(searchText)){
                        TextFormated = TextFormated +" <span style='font-weight:bold'>"+token+"</span> ";
                    }
                    else{
                        TextFormated = TextFormated +" "+ token;}
                }
            texto = texto + "<div style='margin-top:8px;float:left;height:55px;width:300px;border-bottom:1px solid #bbbab7;padding-bottom:3px'>";
            texto = texto + "<img src='"+status.getUser().getOriginalProfileImageURL()+"' width='55px' height='53px' style='display:inline-block;margin-right:3px'><p style='display:inline-block;margin-left:3px'><b>"+ status.getUser().getName() + "</b>" + ":" + TextFormated +"</p>";
            texto = texto + "</div>";
            //texto = texto + "<hr>";
        }
       }
       catch(TwitterException ex){
           JOptionPane.showMessageDialog(null, ex.getMessage());
       }
        EditorTimeline.setText(texto);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Titulo = new javax.swing.JLabel();
        btnTimeline = new javax.swing.JButton();
        btnMentions = new javax.swing.JButton();
        btnDirectMjs = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnTweet = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        TimelineContainer = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        EditorTimeline = new javax.swing.JEditorPane();
        tweetContainer = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTweet = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        charactersCount = new javax.swing.JLabel();
        lbStatusUpdated = new javax.swing.JLabel();
        InfoContainer = new javax.swing.JPanel();
        lbFavorites = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbTweets = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbFollowers = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbFollowing = new javax.swing.JLabel();
        twLogo = new javax.swing.JButton();
        panelTrends = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Trends = new javax.swing.JEditorPane();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));

        Titulo.setBackground(new java.awt.Color(255, 255, 255));
        Titulo.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Titulo.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        Titulo.setText("TWITTER API TEST");

        btnTimeline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimelineActionPerformed(evt);
            }
        });

        btnMentions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMentionsActionPerformed(evt);
            }
        });

        btnDirectMjs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDirectMjsActionPerformed(evt);
            }
        });

        btnSearch.setBorder(null);
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnTweet.setBorder(null);
        btnTweet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTweetActionPerformed(evt);
            }
        });

        txtSearch.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        EditorTimeline.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jScrollPane2.setViewportView(EditorTimeline);

        org.jdesktop.layout.GroupLayout TimelineContainerLayout = new org.jdesktop.layout.GroupLayout(TimelineContainer);
        TimelineContainer.setLayout(TimelineContainerLayout);
        TimelineContainerLayout.setHorizontalGroup(
            TimelineContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 415, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        TimelineContainerLayout.setVerticalGroup(
            TimelineContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );

        tweetContainer.setBackground(new java.awt.Color(0, 0, 0));

        txtTweet.setColumns(20);
        txtTweet.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtTweet.setRows(5);
        txtTweet.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtTweet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTweetKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtTweet);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Tweet");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        charactersCount.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        charactersCount.setForeground(new java.awt.Color(255, 255, 255));
        charactersCount.setText("140");

        lbStatusUpdated.setForeground(new java.awt.Color(0, 102, 255));
        lbStatusUpdated.setText("Status Updated Sucefully!");

        org.jdesktop.layout.GroupLayout tweetContainerLayout = new org.jdesktop.layout.GroupLayout(tweetContainer);
        tweetContainer.setLayout(tweetContainerLayout);
        tweetContainerLayout.setHorizontalGroup(
            tweetContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tweetContainerLayout.createSequentialGroup()
                .add(tweetContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tweetContainerLayout.createSequentialGroup()
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(50, 50, 50)
                        .add(lbStatusUpdated, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 62, Short.MAX_VALUE)
                        .add(charactersCount))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
                .addContainerGap())
        );
        tweetContainerLayout.setVerticalGroup(
            tweetContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, tweetContainerLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tweetContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tweetContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(charactersCount)
                        .add(lbStatusUpdated, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        InfoContainer.setBackground(new java.awt.Color(0, 0, 0));

        lbFavorites.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lbFavorites.setForeground(new java.awt.Color(255, 255, 255));
        lbFavorites.setText("112");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 204));
        jLabel5.setText("Favorites:");

        lbTweets.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lbTweets.setForeground(new java.awt.Color(255, 255, 255));
        lbTweets.setText("2139");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 204));
        jLabel4.setText("Tweets:");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 204));
        jLabel2.setText("Followers:");

        lbFollowers.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lbFollowers.setForeground(new java.awt.Color(255, 255, 255));
        lbFollowers.setText("120");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 204));
        jLabel3.setText("Following:");

        lbFollowing.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lbFollowing.setForeground(new java.awt.Color(255, 255, 255));
        lbFollowing.setText("200");

        org.jdesktop.layout.GroupLayout InfoContainerLayout = new org.jdesktop.layout.GroupLayout(InfoContainer);
        InfoContainer.setLayout(InfoContainerLayout);
        InfoContainerLayout.setHorizontalGroup(
            InfoContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(InfoContainerLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .add(InfoContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(jLabel4)
                    .add(jLabel5))
                .add(6, 6, 6)
                .add(InfoContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lbFollowers)
                    .add(lbFollowing)
                    .add(lbTweets)
                    .add(InfoContainerLayout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(lbFavorites)))
                .addContainerGap())
            .add(InfoContainerLayout.createSequentialGroup()
                .addContainerGap()
                .add(twLogo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        InfoContainerLayout.setVerticalGroup(
            InfoContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, InfoContainerLayout.createSequentialGroup()
                .add(twLogo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(InfoContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(InfoContainerLayout.createSequentialGroup()
                        .add(jLabel2)
                        .add(6, 6, 6)
                        .add(jLabel3)
                        .add(6, 6, 6)
                        .add(jLabel4)
                        .add(6, 6, 6)
                        .add(jLabel5))
                    .add(InfoContainerLayout.createSequentialGroup()
                        .add(lbFollowers)
                        .add(6, 6, 6)
                        .add(lbFollowing)
                        .add(6, 6, 6)
                        .add(lbTweets)
                        .add(6, 6, 6)
                        .add(lbFavorites)))
                .add(27, 27, 27))
        );

        panelTrends.setBackground(new java.awt.Color(0, 0, 0));

        Trends.setBackground(java.awt.Color.black);
        jScrollPane3.setViewportView(Trends);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 204));
        jLabel6.setText("Trends:");

        org.jdesktop.layout.GroupLayout panelTrendsLayout = new org.jdesktop.layout.GroupLayout(panelTrends);
        panelTrends.setLayout(panelTrendsLayout);
        panelTrendsLayout.setHorizontalGroup(
            panelTrendsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelTrendsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelTrendsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 234, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        panelTrendsLayout.setVerticalGroup(
            panelTrendsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelTrendsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtSearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 411, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(Titulo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 208, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(btnTimeline, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(btnMentions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(btnDirectMjs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(btnSearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(btnTweet, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(TimelineContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(tweetContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(InfoContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(panelTrends, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(17, 17, 17)
                .add(Titulo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnTimeline, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnMentions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnDirectMjs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnTweet, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12)
                .add(txtSearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(InfoContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(panelTrends, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(TimelineContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(7, 7, 7)
                        .add(tweetContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        tweetContainer.setVisible(false);
        TimelineContainer.setVisible(false);
        txtSearch.setVisible(true);
        InfoContainer.setVisible(false);
        setDeselected();
        btnSearch.setIcon(imgSearchSelected);
        panelTrends.setVisible(false);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        TimelineContainer.setVisible(true);
        SearchTweets(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnTimelineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimelineActionPerformed
        txtSearch.setVisible(false);
        tweetContainer.setVisible(false);
        TimelineContainer.setVisible(true);
        fillTimeLine("timeline");
        InfoContainer.setVisible(true);
        setDeselected();
        btnTimeline.setIcon(imgTimelineSelected);
        panelTrends.setVisible(true);
    }//GEN-LAST:event_btnTimelineActionPerformed

    private void btnMentionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMentionsActionPerformed
       txtSearch.setVisible(false);
       tweetContainer.setVisible(false);
       TimelineContainer.setVisible(true);
       fillTimeLine("mentions");
       InfoContainer.setVisible(true);
       setDeselected();
       btnMentions.setIcon(imgMentionsSelected);
        panelTrends.setVisible(true);
    }//GEN-LAST:event_btnMentionsActionPerformed

    private void btnDirectMjsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDirectMjsActionPerformed
      txtSearch.setVisible(false);
      tweetContainer.setVisible(false);
      TimelineContainer.setVisible(true);
      InfoContainer.setVisible(true);
      setDeselected();
      btnDirectMjs.setIcon(imgDirectMjsSelected);
       panelTrends.setVisible(true);
    }//GEN-LAST:event_btnDirectMjsActionPerformed

    private void btnTweetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTweetActionPerformed
        lbStatusUpdated.setText("");
        tweetContainer.setVisible(true);
        txtSearch.setVisible(false);
        TimelineContainer.setVisible(false);
        tweetContainer.setLocation(TimelineContainer.getLocation());
        txtTweet.setText("");
        charactersCount.setText("140");
        charactersCount.setForeground(Color.white);
        lbStatusUpdated.setVisible(false);
        InfoContainer.setVisible(false);
        panelTrends.setVisible(false);
    }//GEN-LAST:event_btnTweetActionPerformed

    private void txtTweetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTweetKeyReleased
        int totalCharacters = 0;
        totalCharacters = 140-txtTweet.getText().length();
        if (totalCharacters<=20)
            charactersCount.setForeground(Color.red);
        charactersCount.setText(totalCharacters+"");
        if(evt.getKeyCode()==evt.VK_AT)
        {
            //if pressed @ call following
        }
    }//GEN-LAST:event_txtTweetKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(txtTweet.getText().length()==0){
            JOptionPane.showMessageDialog(null,"Your tweet can not be blank","Tweet Blank", JOptionPane.INFORMATION_MESSAGE);
            return; }
        if(txtTweet.getText().length()>140){
            JOptionPane.showMessageDialog(null,"Your tweet can not contain more than 140 Characteres","Tweet Too Large", JOptionPane.INFORMATION_MESSAGE);
            return;}

        postTweet();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void postTweet()
    {
        try{
        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();
        Status status = twitter.updateStatus(txtTweet.getText());
        lbStatusUpdated.setVisible(true);
        lbStatusUpdated.setText("Status Updated Successfully!");        
        }
        catch(TwitterException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane EditorTimeline;
    private javax.swing.JPanel InfoContainer;
    private javax.swing.JPanel TimelineContainer;
    private javax.swing.JLabel Titulo;
    private javax.swing.JEditorPane Trends;
    private javax.swing.JButton btnDirectMjs;
    private javax.swing.JButton btnMentions;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTimeline;
    private javax.swing.JButton btnTweet;
    private javax.swing.JLabel charactersCount;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbFavorites;
    private javax.swing.JLabel lbFollowers;
    private javax.swing.JLabel lbFollowing;
    private javax.swing.JLabel lbStatusUpdated;
    private javax.swing.JLabel lbTweets;
    private javax.swing.JPanel panelTrends;
    private javax.swing.JButton twLogo;
    private javax.swing.JPanel tweetContainer;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextArea txtTweet;
    // End of variables declaration//GEN-END:variables
}
