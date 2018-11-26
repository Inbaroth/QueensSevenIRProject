package FilesOperation;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test{

    public static void main(String[] args) {
        String line = "<TEXT> for 112 Million company, Vacation4U 2 3/4% is preety bad, on 14 MAY, They spent 55 Trillion, While 320 bn Dollars waisted on drugs, BAD Step-By-Step, 2-4 times. From 10 1/2-11,000,122. Between 12 1/2 and 4,000,123. </TEXT>";
        String line1 = "<TEXT> 12.555 kids. Hey 1) he said: hello frein hey. </TEXT>";
        String line2 = "<TEXT> For. </TEXT>";
        String text = getText();
        String text1 = getText1();
        String text2 = getText2();
        String text3 = getText3();
        String text4 = getText4();
        String text5 = getText5();
        Parse p = new Parse();
        long startTime = System.nanoTime();
//        ReadFile reader = new ReadFile("C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אחזור מידע\\פרויקט מנוע חיפוש\\corpus\\corpus");
//        p.parsing("1",text,"inbar");
        p.parsing("1",text,"inbar1");
//        p.parsing("1",text2,"inbar2");
//        p.parsing("1",text3,"inbar3");
//        p.parsing("1",text4,"inbar4");
//        p.parsing("1",line2,"inbar5");
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;
        System.out.println(duration);
        System.out.println("*******************");


//        Map<String, HashMap<String,Integer>> map = reader.getParser().getTermsMap();
        Map<String, HashMap<String,Integer>> map = p.getTermsMap();
        for (Map.Entry<String,HashMap<String,Integer>> entry:map.entrySet()) {
            System.out.print(entry.getKey()+ ": ");
            for (Map.Entry<String,Integer> doc: entry.getValue().entrySet()) {
                System.out.print ("<" + doc.getKey() + " , " + doc.getValue() + "> -> ");
            }
            System.out.println();
        }
        duration = duration/60000;
        System.out.println(duration);

    }

    private static String getText() {
        return "<DOC>\n" +
                "<DOCNO> FBIS3-1337 </DOCNO>\n" +
                "<HT>    \"drafr058_c_94010\" </HT>\n" +
                "\n" +
                "\n" +
                "<HEADER>\n" +
                "<AU>   FBIS-AFR-94-058 </AU>\n" +
                "Document Type:Daily Report \n" +
                "<DATE1>  23 Mar 1994 </DATE1>\n" +
                "\n" +
                "</HEADER>\n" +
                "\n" +
                "<F P=100> REPUBLIC OF SOUTH AFRICA </F>\n" +
                "<H3> <TI>   Joint Ciskei Administrator Finca on Situation </TI></H3>\n" +
                "<F P=102>  MB2403165894 Johannesburg SABC CCV Television Network in \n" +
                "English 1730 GMT 23 Mar 94 </F>\n" +
                "\n" +
                "<F P=103> MB2403165894 </F>\n" +
                "<F P=104>  Johannesburg SABC CCV Television Network </F>\n" +
                "\n" +
                "\n" +
                "<TEXT>\n" +
                "Language: <F P=105> English </F>\n" +
                "Article Type:BFN \n" +
                "\n" +
                "<F P=106> [Telephone interview with newly appointed joint Ciskei </F>\n" +
                "administrator Reverend Bongani Finca by CCV TV announcer Astrid \n" +
                "Ascar -- live] \n" +
                "  [Text] [Ascar] Good evening, Reverend Finca. Welcome to \n" +
                "Newsline. \n" +
                "  [Finca] Good evening. \n" +
                "  [Ascar] As I asked Mr. Goosen earlier, plans for the \n" +
                "immediate future. I'd like to ask you again what do you think \n" +
                "can be done to address the concerns of the civil servants as \n" +
                "regards their pensions? \n" +
                "  [Finca] There is a task force already set up by the TEC \n" +
                "[Transitional Executive Council] for that purpose and I am led \n" +
                "to believe that that task force is already addressing that \n" +
                "issue. We, as the joint administrators, will be lending our \n" +
                "support to what the task force is going to be doing. \n" +
                "  [Ascar] Rev. Finca, do you think there are any similarities \n" +
                "between the situation in the Ciskei and the situation in \n" +
                "Bophuthatswana not too long ago? \n" +
                "  [Finca] There are similarities, and there are, of course, \n" +
                "differences. The similarities lie in the fact that there is a \n" +
                "popular uprising of dissatisfaction with these Bantustan \n" +
                "dictators. The similarities are in the fact that in Ciskei there \n" +
                "has been, what I believe, an acceptance by Brigadier Gqozo that \n" +
                "he can no longer control the situation and has voluntarily \n" +
                "stepped down. \n" +
                "  [Ascar] Do you not see a pattern emerging in the independent \n" +
                "states? Is there not a fear that this could spill over to other \n" +
                "areas? \n" +
                "  [Finca] I don't know whose fear that is. Is that the fear of \n" +
                "those who are in authority or is that the fear of the people? I \n" +
                "think there is a general trend developing in our country that \n" +
                "democracy has to be dictated to by the people on the ground. \n" +
                "  [Ascar] Rev. Finca, would you corroborate what Mr. Goosen \n" +
                "said, that the Brigadier is in fact not under house arrest? \n" +
                "  [Finca] I'm not as conversant with the facts of what has \n" +
                "been \n" +
                "happening today as my colleague Mr. Goosen is. I've just come \n" +
                "in this morning from Johannesburg. I've been attending a \n" +
                "meeting there, so I believe that Mr. Goosen's version of what is \n" +
                "happening will by and large be correct. He has been closer to \n" +
                "the situation than myself, for today. \n" +
                "  [Ascar] You have been in the Ciskei today. Are you able to \n" +
                "ascertain whether there are indeed Ciskeian Defense Force \n" +
                "loyalists who are still supporting the brigadier? \n" +
                "  [Finca] I am not able to comment on that. I'll be going to \n" +
                "Bisho for the first time tomorrow morning. I came in, as I say, \n" +
                "from Johannesburg this morning, and I have never been to Bisho. \n" +
                "  [Ascar] Rev. Finca, we thank you for your participation. \n" +
                "Thanks for joining us. \n" +
                "  [Finca] Thank you. \n" +
                "\n" +
                "</TEXT>\n" +
                "\n" +
                "</DOC>\n";
    }
    private static String getText1(){
        return "<TEXT>\n" +
                "SUMMARY \n" +
                "\n" +
                "                          In a string of confrontational actions and pronouncements, \n" +
                "Pyongyang has raised the stakes further in its high-stakes \n" +
                "nuclear issues game by threatening to withdraw from the Nuclear \n" +
                "Nonproliferation Treaty (NPT).  Although apparently unwilling to \n" +
                "publicly concede that its own recent actions have damaged the \n" +
                "chances for resumption of high-level U.S.-North Korea dialogue, \n" +
                "Pyongyang appears to be trying to somehow salvage bilateral talks \n" +
                "with Washington. \n" +
                "\n" +
                "                        END SUMMARY \n" +
                "\n" +
                "  Pyongyang Threatens NPT Withdrawal, Calls for U.S. Talks \n" +
                "\n" +
                "\n" +
                "Pyongyang's threat to withdraw from the NPT came in an \n" +
                "authoritative Foreign Ministry spokesman's statement issued on 21 \n" +
                "March (Pyongyang radio, 21 March).  In the statement Pyongyang \n" +
                "said it will \"carry into practice\" its declaration of NPT \n" +
                "withdrawal announced on 12 March 1993 under certain conditions: \n" +
                "\n" +
                "+ If the United States refuses bilateral talks with North Korea \n" +
                "and resumes Team Spirit, thereby \"increasing its nuclear threat\" \n" +
                "against the DPRK. \n" +
                "\n" +
                "+ If the IAEA \"further expands it unfairness\" by \"distorting\" the \n" +
                "results of its recent inspections in North Korea and resorts to \n" +
                "\"forcible measures and pressures.\" \n" +
                "\n" +
                "The 21 March statement, which for the first time acknowledged \n" +
                "that the DPRK-U.S. talks scheduled for the same day in Geneva did \n" +
                "not take place, also repeated familiar North Korean charges of \n" +
                "U.S. violations of previous agreements with North Korea and \n" +
                "declared that Pyongyang will \"no longer\" be duty-bound to ensure \n" +
                "continuity of NPT safeguards. \n" +
                "\n" +
                "Hint of Moderation     Possibly seeking to soften its threat--and \n" +
                "thus somehow leave the door ajar for dialogue with Washington- \n" +
                "-the statement seemed to imply that as long as there is any hope \n" +
                "of resuming bilateral talks with Washington, Pyongyang may \n" +
                "refrain from taking the final step of NPT withdrawal.  North \n" +
                "Korean withdrawal from the NPT, the statement said, would occur \n" +
                "only if the United States \"persists in avoiding\" bilateral talks \n" +
                "\"to the end.\"  Underscoring Pyongyang's reluctance to \n" +
                "definitively rule out future talks, the statement asserted that \n" +
                "the DPRK is \"in no hurry at all\"--presumably, to foreclose \n" +
                "avenues of dialogue with Washington--\"even if the DPRK-U.S. talks \n" +
                "are not held.\"  In addition, in contrast to North Korean \n" +
                "pronouncements on the inter-Korean talks (see following article), \n" +
                "the statement, like previous pronouncements on Washington's \n" +
                "policy, refrained from extreme polemical attacks on Washington. \n" +
                "\n" +
                "Pyongyang media also appeared to react with similar \n" +
                "circumspection to recent statements by high U.S. officials \n" +
                "criticizing the North.  For example, monitored North Korean media \n" +
                "so far have not reported or commented on CIA Director Woolsey's \n" +
                "charges that Pyongyang has been exporting weapons of mass \n" +
                "destruction, is supporting terrorism, and may already possess \n" +
                "nuclear weapons (Yonhap, 18 March). \n" +
                "\n" +
                "Implications     Pyongyang clearly prefers to deal only with \n" +
                "Washington on the nuclear issue.  However, it seems unwilling or \n" +
                "unable to acknowledge that its own dilatory and confrontational \n" +
                "tactics are damaging the prospects for the dialogue it is \n" +
                "seeking. \n" +
                "\n" +
                "  Pyongyang Breaks Off Talks With Seoul, Threatens War \n" +
                "\n" +
                "                             SUMMARY \n" +
                "\n" +
                "Accusing Seoul of engaging in pressure tactics, Pyongyang has \n" +
                "abruptly broken off bilateral talks and issued threats of war- \n" +
                "-threats that it subsequently appeared to moderate.  North Korea \n" +
                "apparently hopes to gain from what it sees as differences within \n" +
                "the ROK Government and between Seoul and Washington on the \n" +
                "nuclear issue. \n" +
                "\n" +
                "                            END SUMMARY \n" +
                "\n" +
                "The North came to Panmunjom on 19 March clearly prepared to break \n" +
                "off the ongoing negotiations with the South on the exchange of \n" +
                "presidential envoys.  The North Korean chief delegate--identified \n" +
                "in Seoul media as Pak Yong-su--not only reinstated demands that \n" +
                "he had previously indicated would be retracted, but also insisted \n" +
                "that the ROK Government \"apologize\" to all Koreans--a formulation \n" +
                "Pyongyang had typically used in the past to signal its \n" +
                "noninterest in dialogue with Seoul and Tokyo--this time for \n" +
                "allegedly obstructing DPRK-U.S. high-level talks (Pyongyang \n" +
                "radio, 19 March). \n" +
                "\n" +
                "The North's main complaint against the South focused on what Pak \n" +
                "described as Seoul's \"sudden change of attitude\" toward adoption \n" +
                "of \"tough measures\" against Pyongyang.  In particular, Pak said \n" +
                "that North Korea's pique was based on reports of the 17 March \n" +
                "\"high-level strategic meeting\" in Seoul, which had reportedly \n" +
                "discussed holding Team Spirit, the deployment of Patriot \n" +
                "missiles, and international sanctions.  Pak equated this alleged \n" +
                "change in the South's attitude to \"a grave crime\" and claimed \n" +
                "that it necessitated the reimposition of the four demands made \n" +
                "earlier by the North--demands that Pak had previously admitted \n" +
                "were \"barriers\" to envoy exchanges erected by Pyongyang itself. \n" +
                "(The North had demanded that the South refrain from 1) staging \n" +
                "large-scale nuclear exercises, 2) introducing new weapons, \n" +
                "including the Patriot missile, and 3) using threats of \n" +
                "international sanctions.  The North had also demanded previously \n" +
                "that 4) the South retract ROK President Kim Yong-nam's remarks \n" +
                "about not shaking hands with North Korean officials.) \n" +
                "\n" +
                "Demand for Apology     Signaling that Pyongyang is no longer \n" +
                "interested in continuing dialogue with Seoul, Pak accused the \n" +
                "South of having used the envoy exchange talks \"solely\" for the \n" +
                "purpose of derailing DPRK-U.S. high-level talks.  He went on to \n" +
                "\"strongly insist\" that the South \"frankly admit\" this \"dark \n" +
                "intention\" and \"apologize before the nation.\"  Attempting to \n" +
                "place the onus of any breakdown of dialogue on the South, Pak \n" +
                "further accused Seoul of having in effect adopted \"a declaration \n" +
                "of abandonment of the special envoys exchange, a declaration of \n" +
                "an all-out confrontation with us, and a declaration of war.\" \n" +
                "\n" +
                "Pyongyang radio on 19 March depicted Pak as making his most \n" +
                "provocative remarks only in response to remarks the South made at \n" +
                "the 19 March session.  According to the radio, the South side had \n" +
                "said that should the envoy exchange talks be discontinued, \"there \n" +
                "is no knowing what danger would materialize.\"  To this Pak \n" +
                "reportedly replied, \"Don't worry about it.  What do you think the \n" +
                "South is?  If the North suffers damage, do you think the South \n" +
                "will go unscathed?\"  Pak went on to pledge unspecified \"immediate \n" +
                "and decisive countermeasures of self-defense\" in case \"some \n" +
                "powers impose sanctions on us or otherwise provoke us.\"  The \n" +
                "radio said Pak coupled his pledge with a \"stern warning\" not to \n" +
                "take the North Korean threats lightly, quoting him as saying, \"we \n" +
                "do not know how to engage in idle talk. \"(SEE NOTE) \n" +
                "\n" +
                "(NOTE: Seoul's government-run KBS-1 television on 19 March \n" +
                "broadcast video recording of even more inflammatory remarks by \n" +
                "Pak that were captured by closed-circuit television coverage of \n" +
                "the meeting.  In the video, Pak told his Southern counterpart, \n" +
                "Song Yong-tae, to \"give a careful consideration to the \n" +
                "consequences of a war,\" and warned that \"Seoul is not far from \n" +
                "here.  If war breaks out, it will turn into a sea of fire.  Mr. \n" +
                "Song, it will probably difficult for you, too, to survive.\" \n" +
                "Pyongyang media have not been observed to report on this portion \n" +
                "of Pak's remarks.) \n" +
                "\n" +
                "Moderation of Threats     Pyongyang subsequently appeared to \n" +
                "backpedal a bit on its threats of war.  In a 21 March statement \n" +
                "issued in the name of the North Korean delegation to the inter- \n" +
                "Korean contacts, which again chastised the South for \"suddenly\" \n" +
                "assuming what it described as a hardline stance, the North made \n" +
                "only passing references to the possibility of war, implying \n" +
                "instead that the downfall of the ROK government will come from \n" +
                "revolt within the South (Pyongyang radio, 21 March).  The \n" +
                "statement warned the ROK to realize that \"no dictators\" in the \n" +
                "South had \"survived committing the antinational act of betraying \n" +
                "fellow countrymen in collusion with outside forces\" and that \n" +
                "\"these flunkeyist, nation-sellers have met a bitter end.\" \n" +
                "North Korean Motives     Pyongyang may calculate that its \n" +
                "brinksmanship will sufficiently split opinions within the South \n" +
                "Korean government to make a firm stand against the North \n" +
                "difficult.  For the past few months, Seoul media reporting on \n" +
                "relations with the North has indicated that there are serious \n" +
                "divisions within the Seoul government over how to deal with \n" +
                "Pyongyang.  The latest of these indications came shortly after a \n" +
                "12 March inter-Korean meeting.  The Seoul daily Hanguk Ilbo on 13 \n" +
                "March cited an unnamed ROK \"government official\" as saying that \n" +
                "the South and the United States \"believe\" that by failing to \n" +
                "agree to the exchange of envoys with Seoul, Pyongyang had \n" +
                "\"unilaterally invalidated\" an agreement reached with Washington \n" +
                "on resuming high-level talks in Geneva later in the month. \n" +
                "Apparently alarmed by the possible effects such remarks could \n" +
                "have on the inter-Korean talks, other \"government officials\" were \n" +
                "cited the next day by the South Korean news agency Yonhap as \n" +
                "advocating a different approach (14 March).  One of them was \n" +
                "quoted as saying that Seoul should not consider it \"a violation \n" +
                "of the North Korea-U.S. agreement\" even if North Korea \"refused\" \n" +
                "the envoy exchange outright.  All that would happen, he \n" +
                "reportedly said, would be that \"the date of the third round \n" +
                "[U.S.-North Korea] meeting would be delayed\" until the envoy \n" +
                "exchange was realized. \n" +
                "\n" +
                "In addition, there has been intermittent South Korean reporting \n" +
                "of a division of views between Seoul and Washington that could \n" +
                "have emboldened Pyongyang.  Most recently, for instance, Yonhap \n" +
                "on 16 March cited Kim Tae-chung, a \"retired\" opposition leader \n" +
                "and former presidential candidate, as \"lashing out\" at U.S. \n" +
                "\"hardliners\" for allegedly jeopardizing lives of Koreans in their \n" +
                "pursuit of confrontation with North Korea on the nuclear issue. \n" +
                "Similarly, in an article datelined Washington, the Seoul daily \n" +
                "Choson Ilbo on 13 March claimed that the U.S. Congress has \n" +
                "concluded that differences between Washington and Seoul over how \n" +
                "to deal with the North Korean nuclear issue are serving as a \n" +
                "\"stumbling block\" in the U.S. Government's formulation of its \n" +
                "policy toward Pyongyang--reporting that could have encouraged \n" +
                "Pyongyang to reinforce its constant attempt to drive a wedge \n" +
                "between Washington and Seoul. \n" +
                "\n" +
                "Outlook     Pyongyang media treatment of the 19 March meeting \n" +
                "seems aimed at portraying the North Korean leadership as fully \n" +
                "prepared to face the worst case scenario.  By declaring \n" +
                "Pyongyang's willingness to destroy Seoul in a conflict, the North \n" +
                "Korean leadership may hope to frighten officials in the South \n" +
                "into advocating concessions and thus further aggravate a \n" +
                "perceived division between the United States and South Korea. \n" +
                "\n" +
                "(AUTHOR:  YIM.  QUESTIONS AND/OR COMMENTS, PLEASE CALL CHIEF, \n" +
                "ASIA DIVISION ANALYSIS TEAM, (703) 733-6534.) \n" +
                "EAG/HEBBEL/sdj 23/0017Z MAR \n" +
                "\n" +
                "</TEXT>\n";
    }
    private static String getText2(){
        return "<TEXT>\n" +
                "SUMMARY \n" +
                "                            Seeking to exploit what he perceives to be a rift in the \n" +
                "coalition that liberated Kuwait, President Saddam Husayn has \n" +
                "presented the UN Security Council with a virtual ultimatum, \n" +
                "threatening implicitly that if it fails to take significant steps in \n" +
                "coming weeks toward lifting the sanctions, Iraq may cease to \n" +
                "cooperate with the UN Special Commission and adopt a confrontational \n" +
                "posture.  Coming amid signs that the sanctions are causing \n" +
                "increasing economic difficulties in Iraq, Saddam's move reflects his \n" +
                "impatience with the Security Council's failure to lift the sanctions \n" +
                "in response to Baghdad's diplomacy and selective compliance, his \n" +
                "desire to frustrate effective future monitoring of Iraqi weapons \n" +
                "programs, and his unwillingness to make further concessions in \n" +
                "compliance with UN resolutions -- such as recognizing the border \n" +
                "with Kuwait. \n" +
                "                       END SUMMARY \n" +
                "   In a 13 March speech, which Iraqi media played up as an ultimatum \n" +
                "threatening confrontation if a date for lifting the sanctions is not \n" +
                "set, Saddam demanded that the Security Council implement Paragraph \n" +
                "22 of Resolution 687 -- allowing Iraq to resume its oil sales -- \n" +
                "\"immediately and without any conditions.\"  He warned that if Deputy \n" +
                "Prime Minister Tariq 'Aziz returns from the UN Security Council \n" +
                "session without the Council's agreement to activate Paragraph 22, \n" +
                "then Iraq's people and leadership would have \"no choice but to \n" +
                "decide what they believe will give them hope . . . in the direction \n" +
                "they believe is sound\" (Baghdad radio). \n" +
                "   Media Highlight Speech \n" +
                "   Tightly controlled Iraqi media immediately began to portray \n" +
                "Saddam's speech as an ultimatum and to prepare the Iraqi public for \n" +
                "confrontation.  For example: \n" +
                "   -- The daily Babil, owned by Saddam's son 'Udayy, said that \n" +
                "Saddam's speech \"defined the contents of the decisive Iraqi \n" +
                "confrontation with the enemies\" (Iraqi News Agency [INA], 17 March). \n" +
                "   -- The daily Al-Qadisiyah (See Note 1) claimed Saddam's speech \n" +
                "gave the Security Council \"the last chance . . . to lift the \n" +
                "sanctions without delay or conditions\" (INA, 15 March). \n" +
                "   (Note 1) Around mid-September 1993, Al-Qadisiyah was removed \n" +
                "from the supervision of the Defense Ministry and affiliated with the \n" +
                "Information Ministry, which now controls this paper as well as Al- \n" +
                "Jumhuriyah (Al-'Iraq, 18 September 1993).  Probably more than an \n" +
                "economy measure, this change, made by a Revolution Command Council \n" +
                "decree, may have been designed to limit the political power of the \n" +
                "Defense Ministry. \n" +
                "   -- Interpreting Saddam's speech as threatening an end to or \n" +
                "reduction in Iraqi cooperation with the United Nations, the Ba'th \n" +
                "Party daily Al-Thawrah said that if the Security Council does not \n" +
                "lift the sanctions, Iraq will choose \"a new road, different . . . in \n" +
                "terms of its approach to [UN] resolutions and committees,\" a road \n" +
                "that will prove Iraq has \"other options\" aside from cooperating with \n" +
                "\"colonialist interpretations of UN resolutions\" (INA, 15 March). \n" +
                "   Earlier Signs \n" +
                "   Saddam had already foreshadowed a possible change of policy in \n" +
                "his 16 January Gulf war anniversary speech, in which he pledged that \n" +
                "\"we will not relinquish our people in the northern part of the \n" +
                "homeland, nor will we stand idle toward the continuation of the \n" +
                "blockade and the violation of our sovereignty in southern Iraq.\"  In \n" +
                "that speech, he also warned \"all evildoers, masters and satellites\" \n" +
                "- and especially \"some evildoers from Saudi Arabia and Kuwait\" -- \n" +
                "not to \"miscalculate\" because \"the punishment of the criminals is an \n" +
                "eye for an eye and a tooth for a tooth\" (Baghdad radio). \n" +
                "   Explaining the Shift \n" +
                "   In his 13 March speech, Saddam explained that a shift from \n" +
                "diplomacy to confrontation may prove necessary because Iraqis who \n" +
                "advocated concessions seem to have been wrong while Iraqi hardliners \n" +
                "seem to have been right.  He said Iraq had \"thus far\" employed \n" +
                "\"diplomatic efforts\" to achieve \"a better level of understanding by \n" +
                "the Security Council\" because \"many people in our country\" had \n" +
                "\"expected\" that this policy \"would make some people, whose hearts \n" +
                "have no feeling, show a flexible position and open the doors again \n" +
                "to the Iraqi people.\" \n" +
                "   Saddam contrasted Iraqis who favored the diplomatic policy with \n" +
                "other Iraqis who were \"quite convinced that the criminal level of \n" +
                "aggression and the way sanctions were implemented . . . came from \n" +
                "minds without any conscience.\"  He said these Iraqis, evidently now \n" +
                "including himself, believed that \"despite Iraq's offers and \n" +
                "responses,\" its diplomats would discover that \"there is nothing \n" +
                "being given in return\" and that the Security Council's Paragraph 22 \n" +
                "is being used merely as a \"tactical cover to make Iraq fulfill its \n" +
                "obligations\" while the Security Council fails to fulfill its own \n" +
                "obligations. \n" +
                "   Minimum Demands \n" +
                "   Indicating his minimum demands, Saddam castigated UN Special \n" +
                "Commission Chairman Rolf Ekeus for failing to provide a timetable \n" +
                "stating when the probationary period for monitoring Iraqi weapons \n" +
                "production would begin and end.  Saddam insisted that the Iraqis \n" +
                "must know when they can start selling oil so that they \"will be able \n" +
                "to make their plans, see what they can do, and prepare themselves \n" +
                "psychologically and economically for the required duration.\" \n" +
                "   Saddam also revealed his resentment that Ekeus was working to \n" +
                "turn UN Security Council Resolution 715 into an effective monitoring \n" +
                "instrument.  He claimed Ekeus had said that as soon as Iraq approved \n" +
                "of 715, he would write a report to the Security Council, but when \n" +
                "Iraq approved it, Ekeus said, in Saddam's words, \"we must first \n" +
                "start monitoring and make sure that Iraq is implementing 715 the way \n" +
                "we want -- the way Ekeus wants  -- in order to make sure Iraq is \n" +
                "committed.\"  Saddam claimed that the probationary monitoring period \n" +
                "should have started as soon as the two cameras to monitor missile \n" +
                "testing were installed and turned on. \n" +
                "   'Aziz Letter \n" +
                "   In a letter to the UN Security Council president on 16 March, \n" +
                "'Aziz reiterated that Iraq \"demands\" that the Council apply \n" +
                "Paragraph 22 \"immediately and without any additional restrictions or \n" +
                "conditions\" and that it \"reject the tendentious political \n" +
                "interpretations of its resolutions that some states are seeking to \n" +
                "impose on the Council.\"  Rejection of Iraq's demand, he said, would \n" +
                "\"certainly lead to a reassessment of the situation and to an action \n" +
                "[tasarruf] that will protect the Iraqi people from the harm to which \n" +
                "they are being exposed.\" \n" +
                "   Indicating that at least for now Iraq will not recognize Kuwait \n" +
                "and the Iraq-Kuwait boundary, as required by UN Security Council \n" +
                "Resolution 833, 'Aziz said that if the sanctions and the no-fly \n" +
                "zones are lifted, the issue of Kuwait \"could then be discussed in \n" +
                "accordance with the rules regulating relations among states\" \n" +
                "(Baghdad radio, 17 March). (See Box 1) \n" +
                "   Perceived Rift \n" +
                "   Baghdad appears to believe that now is an opportune moment to \n" +
                "exploit what it perceives as a serious rift within the alliance that \n" +
                "liberated Kuwait.  For example, the Information Ministry's \n" +
                "mouthpiece Al-Jumhuriyah has claimed that the United States has \n" +
                "become \"virtually isolated in its calls for the continued \n" +
                "enforcement of sanctions against Iraq, particularly since it has \n" +
                "seen with its own eyes the disintegration of its international \n" +
                "coalition\" (INA, 6 March).  Iraqi media have recently listed France, \n" +
                "Russia, and China as among the countries showing understanding for \n" +
                "Baghdad's position.  In a recent article, Saddam's press secretary \n" +
                "'Abd-al-Jabbar Muhsin advocated using a policy of \"intimidation\" and \n" +
                "\"temptation,\" denying trade benefits, priority in debt payments, and \n" +
                "other benefits to \"any country not currently adopting a just and \n" +
                "equitable stand\" and offering corresponding benefits to countries \n" +
                "now helping Iraq to lift the sanctions (Babil, 24 February). \n" +
                "   Information Ministry First Under Secretary Nuri Najm al-Marsumi \n" +
                "argued in Al-'Iraq -- the daily of the pro-Saddam Kurds -- that \n" +
                "Iraqi diplomacy and U.S. mistakes had helped create \"a serious \n" +
                "fissure within the anti-Iraq alliance\" and that \"current \n" +
                "international and regional circumstances\" provide Iraq with \"a \n" +
                "serious and true opportunity for lifting the unfair embargo.\" \n" +
                "Therefore, he said, Iraq must \"draw the world's attention\" to its \n" +
                "determination and capability \"not to remain idle\" if the sanctions \n" +
                "continue and to its current \"rallying\" around Saddam \"to thwart the \n" +
                "aggression once and for all and to achieve the decisive victory\" (8 \n" +
                "March). (See Box 2) \n" +
                "   Implications \n" +
                "   Saddam clearly is unwilling to recognize Kuwait, as required by \n" +
                "UN Security Council Resolution 833, unable to stay in power without \n" +
                "repressing his people, as required by Resolution 688, and surprised \n" +
                "by Ekeus' determination to treat Resolution 715 as an effective \n" +
                "means of monitoring Iraqi weapons.  Nevertheless, he seems to \n" +
                "believe that to protect his proclaimed status as \"Iraq's historic \n" +
                "leader\" and to avoid being perceived as weak, he needs to manipulate \n" +
                "the Security Council into setting a date for Iraq to resume selling \n" +
                "its oil.  He is therefore hoping that his threat to adopt a new, \n" +
                "presumably more confrontational policy will break up the U.S.-led \n" +
                "coalition and erode the sanctions. \n" +
                "   If his ultimatum is rejected, Saddam will almost certainly feel \n" +
                "compelled to do something to carry out his threat.  He may be \n" +
                "content with an intensified propaganda campaign, directed in \n" +
                "particular to Arab and Islamic countries, and with repeatedly \n" +
                "threatening and then carrying out a gradual reduction or sudden \n" +
                "cessation in Iraq's present degree of cooperation with the United \n" +
                "Nations.  However, one cannot rule out Saddam's giving up on efforts \n" +
                "to lift sanctions and therefore seeking a victory in another area, \n" +
                "such as a move to regain control of Iraqi Kurdistan. \n" +
                "                 Box 1:  Denying Kuwaiti Legitimacy \n" +
                "   Saddam's consideration of a change of policy is based partly on \n" +
                "his unwillingness to go any further to comply with UN resolutions. \n" +
                "For example, Iraqi leaders and media have signaled their continuing \n" +
                "intention not to recognize the legitimacy of Kuwait, its government, \n" +
                "or its border with Iraq. \n" +
                "   -- In an address to pro-Saddam foreign visitors on 18 January, \n" +
                "Saddam personally denied the legitimacy of the Kuwaiti rulers, \n" +
                "claiming that by their own actions they had lost the right to rule. \n" +
                "After accusing them of lying in 1990 in order to make the U.S. \n" +
                "public \"accept the idea of war\" against Iraq, he said that \"when a \n" +
                "Muslim ruler lies, then he has lost every legitimate right to be a \n" +
                "ruler\" (INA). \n" +
                "   -- Commenting on Saddam's statement in his 16 January Gulf war \n" +
                "anniversary speech that \"we will not turn wisdom into a prelude for \n" +
                "weakness and despair,\" Information Ministry First Under Secretary \n" +
                "al-Marsumi explained that, while \"wisdom and necessity\" have led \n" +
                "Iraq \n" +
                "to accept UN Security Council resolutions, Iraq will not simply \n" +
                "agree to \"whatever it is asked for.\"  In particular, it will not \n" +
                "\"guarantee the security of the bandits, the rulers of the Kuwait \n" +
                "governorate and Saudi Arabia\" despite the fact that this is what \n" +
                "\"the Americans want\" (Babil, 2O January). \n" +
                "   -- The practice of referring to Kuwait as the [Iraqi] \n" +
                "\"governorate of Kuwait\" has continued (for example, Al-Jumhuriyah \n" +
                "editor Salah al-Mukhtar in an article in Babil, 3O January; Babil, \n" +
                "1O and 12 February).  More recently, Saddam's press secretary 'Abd \n" +
                "al-Jabbar Muhsin has repeatedly referred to Kuwait as \"Kazimah,\" the \n" +
                "Iraqi-imposed name for Kuwait City under the occupation (Babil, 10 \n" +
                "March). \n" +
                "                   End of Box 1 \n" +
                "               Box 2: Economic Deterioration \n" +
                "   Official efforts to prepare the public for a change in policy \n" +
                "have come amid signs of increasing economic deterioration that \n" +
                "Baghdad perceives as a threat to Iraqi steadfastness and morale. \n" +
                "For example: \n" +
                "   --  A Babil editorial under a pseudonym used by Saddam's son \n" +
                "'Udayy castigated the Cabinet for decisions that allegedly caused \n" +
                "the dinar's exchange rate to plummet between September 1993 and \n" +
                "January 1994, thereby allowing Iraq's \"enemy\" to make \"some gains in \n" +
                "the arena of the economic battle.\"  The editorial, citing a UN \n" +
                "statement that called for deferring any discussion of lifting the \n" +
                "embargo until Iraq's good will had been tested for a year, implied \n" +
                "that Iraq could not last another year unless the dinar's slide is \n" +
                "reversed.  It therefore suggested that the success or failure of a \n" +
                "ministry should be judged by its contribution to the strength or \n" +
                "weakness of the dinar (2O January). \n" +
                "   -- Saddam's press secretary 'Abd-al-Jabbar Muhsin attacked \"a \n" +
                "handful of misled Iraqis\" who \"raise prices whenever the enemy \n" +
                "escalates its onslaught and its conspiracies\" and who have become a \n" +
                "danger to Iraqi \"morale and steadfastness\" (Al-Qadisiyah, 22 \n" +
                "February). \n" +
                "   -- The 1994 budget and investment plan was 25 percent below the \n" +
                "1993 figures (Baghdad radio, 29 December). \n" +
                "                     End of Box 2 \n" +
                "   (AUTHOR:  BUDER.  QUESTIONS AND/OR COMMENTS, PLEASE CALL CHIEF, \n" +
                "NEAR EAST ANALYSIS BRANCH (703) 733-6094) \n" +
                "pf 19/0026z Mar \n" +
                "\n" +
                "</TEXT>\n";
    }
    private static String getText3(){
        return "<TEXT>\n" +
                "SUMMARY \n" +
                "                              Clearly alarmed by the swiftness with which the IAEA is calling a \n" +
                "Board of Governors meeting, Pyongyang has made a public effort to \n" +
                "explain its differences with the IAEA on resolution of the \n" +
                "nuclear inspection issue, an effort apparently motivated by \n" +
                "concern over the need to salvage high-level talks with Washington \n" +
                "and head off possible UN sanctions. \n" +
                "                          END SUMMARY \n" +
                "   Pyongyang's latest authoritative pronouncement on the nuclear \n" +
                "inspection issue, which came in an 18 March Atomic Energy General \n" +
                "Department(SEE NOTE) spokesman's press statement, seemed to \n" +
                "carefully avoid foreclosing the possibility of a negotiated \n" +
                "settlement (Pyongyang radio, 18 March).  Although the statement \n" +
                "carried the standard warning against applying pressure on \n" +
                "Pyongyang and charges of Western plans to \"crush\" the DPRK, its \n" +
                "major complaint seemed to be that the IAEA is convening a Board \n" +
                "of Governors meeting--reportedly for 21 March (Yonhap, 17 March)- \n" +
                "-\"even before\" analyzing the results of the just concluded \n" +
                "inspections, having \"hastily\" reached the \"unjust\" conclusion \n" +
                "that \"it cannot guarantee\" non-diversion of nuclear materials. \n" +
                "   (NOTE: The department was previously known as the Ministry of \n" +
                "Atomic Energy Industry.  Pyongyang media have not been observed \n" +
                "to report on the apparent reorganization.) \n" +
                "   The statement, which made it clear that Pyongyang had allowed the \n" +
                "IAEA inspection this time solely to facilitate DPRK-U.S. high- \n" +
                "level talks,(SEE NOTE) noted areas of basic disagreement with the \n" +
                "IAEA.  This disagreement is due, the statement claimed, to the \n" +
                "inspection team's attempts to go beyond the contents of a 15 \n" +
                "March bilateral agreement reached in Vienna with the IAEA. \n" +
                "According to Pyongyang: \n" +
                "   + The IAEA inspection team unfairly maintained that the \n" +
                "inspections were being conducted under Nuclear Nonproliferation \n" +
                "Treaty (NPT) safeguards.  The North Korean statement claimed that \n" +
                "Pyongyang had agreed only to allow inspections \"purely\" to ensure \n" +
                "continuity of the safeguards, reiterating Pyongyang's contention \n" +
                "that its \"special status\" of having only \"temporarily suspended\" \n" +
                "withdrawal from the NPT exempts it from normal safeguards \n" +
                "obligations. \n" +
                "   + The IAEA's insistence on 1) extracting samples in sealed-off \n" +
                "areas where the seals remained intact; 2) gamma radiation mapping \n" +
                "at all points, instead of selected points; and 3) examining \"the \n" +
                "cooling system,\"  went beyond what is needed to merely ensure \n" +
                "continuity of the safeguards.  Such inspections, the statement \n" +
                "maintained, would have made the inspections \"equivalent\" to \n" +
                "routine and ad hoc inspections required under the safeguards. \n" +
                "   (NOTE: Underscoring Pyongyang's eagerness to resume high-level \n" +
                "bilateral talks with Washington, North Korean Ambassador to \n" +
                "Moscow Son Song-pil has reiterated Pyongyang's contention that \n" +
                "the DPRK-U.S. talks, once held, will serve as \"an important \n" +
                "occasion\" to solve the nuclear issue and will also lead to \"easy \n" +
                "resolution\" of pending inter-Korean issues (Pyongyang radio, 18 \n" +
                "March).) \n" +
                "   Charges of \"Pressure\" Tactics     The statement complained that \n" +
                "the IAEA had sent three messages to Pyongyang during the \n" +
                "inspection, \"threatening\" to \"report to the Board of Governors \n" +
                "that the agency is not in a position to verify non-diversion of \n" +
                "nuclear materials\" unless its demands were met.  However, in \n" +
                "warning against such \"pressure\" tactics, the North Korean \n" +
                "statement said that any attempts at pressuring the DPRK \"would \n" +
                "lead to a situation in which we would have no choice but to take \n" +
                "a decisive measure to counter them.\"  The statement went on to \n" +
                "pledge that Pyongyang will watch to see if the IAEA \"intends to \n" +
                "resolve our nuclear issue fairly or attempts to use it for \n" +
                "political aims\"--a relatively circumspect formulation that would \n" +
                "appear to allow Pyongyang a certain range of latitude in judging \n" +
                "IAEA action on the inspection issue. \n" +
                "   Implications     Although the statement seemed to leave little \n" +
                "room for compromise, it did stop short of an ultimatum, adopting \n" +
                "instead a wait-and-see attitude toward future IAEA action.  These \n" +
                "caveats suggests that Pyongyang is trying to protect the \n" +
                "possibility of engaging in bilateral talks with Washington by \n" +
                "somehow preventing referral of the nuclear issue to the UN \n" +
                "Security Council. \n" +
                "   (AUTHOR:  YIM.  QUESTIONS AND /OR COMMENTS, PLEASE CALL CHIEF, \n" +
                "ASIA DIVISION ANALYSIS TEAM, (703) 733-6534.) \n" +
                "EAG/BIETZ/PF 19/0014z Mar \n" +
                "\n" +
                "</TEXT>\n";
    }
    private static String getText4(){
        return "<TEXT>\n" +
                "SUMMARY \n" +
                "\n" +
                "                               President Boris Yeltsin continues to face serious opposition from \n" +
                "certain regions in his efforts to define a division of central \n" +
                "and local authority.  Several republics failed to approve the \n" +
                "federal constitution in the 12 December referendum and have since \n" +
                "taken fresh steps toward enacting republic constitutions that \n" +
                "claim greater powers for local authorities.  In the case of \n" +
                "Tatarstan, Moscow has dealt with the conflict by signing a \n" +
                "bilateral treaty after prolonged negotiations, but it is not yet \n" +
                "clear if Moscow is willing to conclude such treaties with other \n" +
                "republics. \n" +
                "\n" +
                "                           END SUMMARY \n" +
                "\n" +
                "   Rejection of the federal constitution by voters in several \n" +
                "republics in the 12 December referendum and recent moves to \n" +
                "approve republic constitutions that assert regional authority in \n" +
                "ways incompatible with the Federation constitution demonstrate \n" +
                "that the republics' constitutional conflict with Moscow remains a \n" +
                "serious issue: \n" +
                "\n" +
                "   -- The Tuvan republic held a referendum to approve the republic's \n" +
                "constitution on 12 December, the same day as the referendum on \n" +
                "the federal constitution.  While the Tuvan constitution, approved \n" +
                "by 53.9 percent of the voters in the referendum, formally \n" +
                "acknowledges the republic to be part of the Russian Federation \n" +
                "(Ostankino TV, 27 October 1993), it also \"envisages the right to \n" +
                "self-determination\" (ITAR-TASS, 16 December 1993).  It provides \n" +
                "for a 32-member republic parliament, the Supreme Hural, whose \n" +
                "membership was also elected on 12 December. The republic's \n" +
                "constitution gives parliament authority to decide \"problems of \n" +
                "war and peace, borders,\" and to suspend Russian Federation \n" +
                "legislative acts which \"go beyond the powers envisaged by the \n" +
                "constitution of the Russian Federation, the federal treaty or \n" +
                "other agreements\" (ITAR-TASS, 16 December 1993).  Only 30.5 \n" +
                "percent of Tuvan voters approved the federal constitution on 12 \n" +
                "December. \n" +
                "\n" +
                "   -- The constitution of the republic of Bashkortostan, adopted on \n" +
                "24 December 1993 by 206 out of 227 republic Supreme Soviet \n" +
                "deputies (Segodnya, 28 December 1993; Sovetskaya Rossiya, 25 \n" +
                "December 1993), claims precedence over the federal constitution, \n" +
                "at least in some provisions.  The Bashkortostan constitution \n" +
                "provides that the republic will voluntarily yield to Moscow \n" +
                "certain powers pertaining to interstate relations under terms of \n" +
                "an as yet to be negotiated treaty with the Russian Federation but \n" +
                "retain authority over its judicial system and prosecutor's \n" +
                "office, independent foreign economic and foreign policy affairs, \n" +
                "and entrance into interstate associations (Nezavisimaya Gazeta, \n" +
                "28 December 1993).  The republic's constitution also asserts \"the \n" +
                "land, its contents, natural wealth, and other natural resources \n" +
                "on its territory\" to be \"the property of its multinational \n" +
                "people\" (Sovetskaya Rossiya, 25 December 1993).  Fifty-three \n" +
                "percent of the Bashkortostan electorate voted against the federal \n" +
                "constitution in the 12 December referendum (Nezavisimaya Gazeta, \n" +
                "16 December). \n" +
                "\n" +
                "   -- Tatarstan also rejected the federal constitution on 12 \n" +
                "December as fewer than fourteen percent of the republic's \n" +
                "electorate participated in the referendum (Izvestiya, 14 December \n" +
                "1993).  In November 1992, the republic Supreme Soviet approved a \n" +
                "republic constitution that states that Tatarstan's laws \"take \n" +
                "precedence throughout its territory, unless they conflict with \n" +
                "international commitments of the republic,\" and that the republic \n" +
                "constitution \"is an act of the highest legal force of direct \n" +
                "effect\" and declares the republic a \"sovereign state\" and \n" +
                "\"subject of international law\" (Sovetskaya Tatariya, 13 December \n" +
                "1992).  The constitution associates the republic with the Russian \n" +
                "Federation only within the parameters of a bilateral treaty that \n" +
                "divides Federation and republic authority (see box) (Interfax, 6 \n" +
                "November 1992). \n" +
                "\n" +
                "   Moscow is also engaged in a constitutional conflict with \n" +
                "Sverdlovsk oblast over the oblast's bid to claim republic status. \n" +
                "Under the leadership of former oblast Governor Eduard Rossel, \n" +
                "residents of the Sverdlovsk region are seeking formal recognition \n" +
                "for the Urals republic, favored by 83 percent of voters (with a \n" +
                "55 percent voter turnout) in a 25 April 1993 non-binding \n" +
                "referendum (Interfax, 1 July 1993) and proclaimed by a decision \n" +
                "of the oblast soviet (Izvestiya, 3 July 1993).  Despite being \n" +
                "fired by Yeltsin for backing the move to claim republic status, \n" +
                "Rossel was given a vote of confidence as 61.87 percent of the \n" +
                "Sverdlovsk Oblast electorate endorsed him for a seat in the \n" +
                "Federation Council (Interfax, 13 December 1993).  Rossel has \n" +
                "succeeded in getting a debate on approval of the constitution of \n" +
                "the Urals Republic on the Federation Council's agenda (Izvestiya, \n" +
                "10 February).  Claiming that the establishment of the Urals \n" +
                "Republic and adoption of its constitution were made \"in full \n" +
                "conformity\" with the federal constitution, Rossel asserted he is \n" +
                "\"ready\" to appeal to the Constitutional Court to approve the \n" +
                "republic constitution (ITAR-TASS, 17 January). \n" +
                "\n" +
                "   Tatarstan Negotiations \n" +
                "\n" +
                "   Moscow's response to the conflict with Tatarstan has been to \n" +
                "engage in protracted negotiations with the republic, talks which \n" +
                "have ended in mutual agreement on a treaty.  Culminating three \n" +
                "years of negotiations with Kazan, Moscow's future relations with \n" +
                "the republic will be based on the bilateral \"Treaty on the Mutual \n" +
                "Delegation of Authority and Terms of Reference,\" signed on 15 \n" +
                "February by Yeltsin and Tatarstan President Mintimer Shaymiyev \n" +
                "(ITAR-TASS, 15 February) (See Box). \n" +
                "\n" +
                "   The Moscow-Kazan bilateral treaty is supplemented by twelve \n" +
                "intergovernmental agreements.  According to Kommersant-Daily, the \n" +
                "agreements concern oil production, ownership matters, defense \n" +
                "industries, customs, transportation of oil products, \n" +
                "environmental cooperation, education, the budget, banking system, \n" +
                "foreign economic activity, crime, and military organization (5 \n" +
                "February). \n" +
                "   Treaty as a Model \n" +
                "\n" +
                "   Since the signing of the Russia-Tatarstan bilateral treaty, \n" +
                "Yeltsin and Shakhray have hailed the treaty as a \"precedent\" for \n" +
                "negotiating similar agreements with other components: \n" +
                "\n" +
                "   -- Following the treaty signing, Yeltsin stressed that \"treaties \n" +
                "of this kind are, basically, the sole constitutional way of \n" +
                "removing contradictions between the Russian Federation \n" +
                "constitution and republic constitutions in a civilized way, \n" +
                "without twisting anyone's arm\" (ITAR-TASS, 15 February). \n" +
                "\n" +
                "   -- Shakhray was quoted as saying that \"there is a legal and \n" +
                "economic possibility of the conclusion of treaties similar to \n" +
                "that with Tataria with each component of the Russian Federation,\" \n" +
                "based on \"Article 11 of the Constitution . . . clause 3 of which \n" +
                "says that the terms of reference shall be delineated \"by this \n" +
                "constitution and the Federal and other treaties\" (Segodnya, 25 \n" +
                "February).  According to a 15 February Interfax report, Shakhray \n" +
                "would like to see Federal Assembly adoption of a law on power- \n" +
                "sharing as a way of regulating the process. \n" +
                "\n" +
                "   Detractors characterize the Russia-Tatarstan treaty as a threat \n" +
                "to federalism, saying it will complicate future center-regional \n" +
                "relations and unacceptably weaken the center: \n" +
                "\n" +
                "   -- Sergey Baburin, State Duma deputy and leader of the \n" +
                "unregistered \"Russian Way\" faction, viewed the treaty as a threat \n" +
                "to federalism and submitted a Duma proposal \"On Guarantees of the \n" +
                "Federal System and Provision for Territorial Integrity of the \n" +
                "Russian Federation On Account of the Signing of the Treaty with \n" +
                "Tatarstan,\" which apparently failed for lack of votes. \n" +
                "Explaining his proposal, Baburin was quoted as saying that \"for \n" +
                "the first time the state signed a treaty with a part of itself\" \n" +
                "and that the treaty presents \"a great danger because it means the \n" +
                "state's transition to a confederative system\" (Interfax, 16 \n" +
                "February). \n" +
                "\n" +
                "   -- In a 16 February article, Segodnya commentator Vladimir Todres \n" +
                "suggested that pursuing separate treaties for each region would \n" +
                "produce gridlock and confusion.  He wrote, \"Russia has a further \n" +
                "87 regions, not counting Chechnya.  The question \"If the Tatars \n" +
                "can do it, why can't we?\" is quite natural.  Why should the other \n" +
                "regional leaders swallow this treaty in silence?\" \n" +
                "\n" +
                "   Other Republics \n" +
                "\n" +
                "   While Moscow and Kazan were putting the finishing touches on the \n" +
                "treaty, Moscow still appeared to be pressing the other republics \n" +
                "to revise their constitutions, remaining noncommital on a \n" +
                "willingness to conclude other treaties. \n" +
                "\n" +
                "   In the case of at least one republic, Bashkortostan, Moscow \n" +
                "appears to have opened negotiations on a set of agreements \n" +
                "similar to those it signed with Tatarstan.  According to republic \n" +
                "Prime Minister Anatoliy Kopsov, the signing of the treaty with \n" +
                "Tatarstan \"gave the latest impetus\" to the resumption of talks \n" +
                "between Moscow and Bashkortostan.  He said that the negotiators \n" +
                "were working on a treaty and a package of bilateral economic, \n" +
                "social, and cultural agreements, some of which are \"practically \n" +
                "ready for signing\" (Interfax, 17 March).  Moscow officials have \n" +
                "not been observed to comment on these negotiations. \n" +
                "\n" +
                "   Before the Moscow-Kazan treaty signing, Moscow urged \n" +
                "constitutional reform in a unilateral effort to rein in the \n" +
                "regions.  Members of the Council of Heads of Administrations \n" +
                "under President Yeltsin had reportedly advised the formation of a \n" +
                "commission that would bring the constitutions of republics \"in \n" +
                "line\" with the federal constitution.  The Council cited \"a number \n" +
                "of norms\" in which component constitutions \"contradict\" the \n" +
                "federation's.  Nikolay Medvedev, head of the presidential \n" +
                "Administration's Directorate for Work with the Territories, \n" +
                "placed \"special emphasis\" on twelve republics that still have \n" +
                "\"old\" constitutions (See Note 1), and five newly established \n" +
                "republics, Adygey, Altai, Ingushetia, Karachayevo-Cherkessia, and \n" +
                "Khakassiya, which do not yet have their own constitutions \n" +
                "(Interfax, 6 January; Russian Television, 6 January). \n" +
                "\n" +
                "   (Note 1)  To illustrate his point, Medvedev reportedly cited the \n" +
                "republic of Mordovia, which \"by its constitution is still a \n" +
                "soviet socialist republic\" (Interfax, 6 January).  The Mordovian \n" +
                "SSR has since been renamed the Republic of Mordovia (Ostankino \n" +
                "TV, 26 January). \n" +
                "\n" +
                "   Republics' Response \n" +
                "\n" +
                "   To date, only a few republics have signaled willingness to amend \n" +
                "their draft constitutions in response to Moscow's objections and \n" +
                "the criticism from the Council of Heads of Administrations: \n" +
                "\n" +
                "   -- In its 11 January issue, Izvestiya reported that the \n" +
                "Kabardino-Balkar Republic's parliament approved a package of \n" +
                "proposals from republic President Valeriy Kokov to \"alter\" and \n" +
                "\"supplement\" the republic constitution so that it complies with \n" +
                "the federal constitution. \n" +
                "\n" +
                "   -- According to a 19 February Nezavisimaya Gazeta article, the \n" +
                "Komi Republic constitution \"corresponds\" with the federal \n" +
                "constitution.  Adopted on 17 February by a majority vote of the \n" +
                "republic Supreme Soviet, the republic constitution proclaims Komi \n" +
                "as a sovereign formation within the Russian Federation (Radio \n" +
                "Rossii, 17 February).  According to Russian TV, even before its \n" +
                "adoption the draft constitution for the Komi Republic was \n" +
                "recognized as an attempt to incorporate \"the principles of \n" +
                "territorial sovereignty on the basis of Russian federalism\" (24 \n" +
                "January). \n" +
                "\n" +
                "   -- In a 27 February referendum, 97.6 percent of voters approved \n" +
                "the constitution of Ingushetia, which declares the republic a \n" +
                "\"democratic, rule-of-law state, part of the Russian Federation on \n" +
                "a treaty basis\" (Nezavisimaya Gazeta, 2 March).  Employees of the \n" +
                "republic Justice Ministry had underscored the need to put the \n" +
                "Ingush constitution in line with the federal constitution before \n" +
                "the scheduled referendum (ITAR-TASS, 23 January).  Ingushetia's \n" +
                "conciliatory response may be linked to the federation \n" +
                "government's mediating role in an Ingush-North Ossetian \n" +
                "territorial dispute as an attempt to gain the federation \n" +
                "government's favor. \n" +
                "\n" +
                "   -- The republic of Buryatia reportedly passed a new constitution \n" +
                "that appears to follow the model set by Tatarstan.  Having \n" +
                "received a unanimous vote of confidence by the republic Supreme \n" +
                "Soviet, Buryatia's constitution reportedly states that the \n" +
                "republic is a \"sovereign, democratic, law-governed state within \n" +
                "the Russian Federation,\" and that its relations with Moscow will \n" +
                "be \"built on a constitutional, treaty-based foundation\" \n" +
                "(Ostankino TV, 22 February). \n" +
                "\n" +
                "   -- According to republic Supreme Soviet First Deputy Chairman \n" +
                "Bagaudin Akhmedov, the new constitution of Dagestan calls for \n" +
                "\"the building of a single, sovereign, democratic state within the \n" +
                "Russian Federation (Segodnya, 1 March). \n" +
                "   Implications \n" +
                "\n" +
                "   Despite Yeltsin's and Shakhray's endorsements of the Moscow-Kazan \n" +
                "bilateral treaty as a model, it is not clear that Moscow is \n" +
                "actually prepared to conclude similar treaties with the other \n" +
                "republics.  Although Shakhray has reemerged as a player in \n" +
                "nationality affairs after a period of relative eclipse, his \n" +
                "position within the government remains clouded, somewhat diluting \n" +
                "the value of his endorsement.  The other republics, probably \n" +
                "viewing the treaty as a success for Kazan, will likely press \n" +
                "Moscow to give them a similar deal. \n" +
                "\n" +
                "Box \n" +
                "\n" +
                "                    The Moscow-Kazan Treaty: \n" +
                "                       Who won? Who lost? \n" +
                "\n" +
                "   Officials have made conflicting statements on the specifics on \n" +
                "the bilateral treaty, but media reportage suggests that both \n" +
                "Russia and Tatarstan made concessions in the treaty's final \n" +
                "draft: \n" +
                "\n" +
                "   -- Although Tatarstan may now \"take part in international \n" +
                "relations and foreign economic activity\" (ITAR-TASS, 15 February) \n" +
                "and--according to republic President Mintimer Shaymiyev--the \n" +
                "treaty \"recognizes the right to self-determination . . . and \n" +
                "defense of sovereignty\" (ITAR-TASS, 16 February), the words \n" +
                "sovereign state\" and \"subject of international law\" were \n" +
                "reportedly removed from the treaty draft (Krasnaya Zvezda, 17 \n" +
                "February). \n" +
                "\n" +
                "   -- The treaty reportedly gives Tatarstan \"exclusive\" ownership \n" +
                "and use of the land on its territory and its contents, and all \n" +
                "property (ITAR-TASS, 16 February). \n" +
                "\n" +
                "   -- According to a 17 February Krasnaya Zvezda article, Tatarstan \n" +
                "reportedly had to abandon its plans to establish a \"single- \n" +
                "channel\" taxation system in favor of the \"general federal \n" +
                "scheme.\" \n" +
                "\n" +
                "   -- Although Shaymiyev has reportedly described the outcome of the \n" +
                "negotiations as \"a change in [Tatarstan's] status . . . within \n" +
                "Russia\" (Krasnaya Zvezda, 17 February), Minister of Nationality \n" +
                "Affairs and Regional Policy Sergey Shakhray has countered that \n" +
                "the treaty \"provides neither for upgrading nor for downgrading \n" +
                "the status of Tatarstan\" (Interfax, 18 February). \n" +
                "\n" +
                "   Shaymiyev stated that the Moscow-Kazan treaty will not have to be \n" +
                "ratified, as the federal and Tatarstan constitutions only require \n" +
                "parliamentary ratification of international treaties (Kazanskiye \n" +
                "Vedomosti, 22 February). \n" +
                "\n" +
                "End Box \n" +
                "\n" +
                "(AUTHOR:  NERO.  QUESTIONS AND/OR COMMENTS, PLEASE CALL CHIEF, \n" +
                "ANALYSIS BRANCH, RUSSIA DIVISION (703) 733-6070.) \n" +
                "ENEAG/BLOUGH/JF 18/1958Z MAR \n" +
                "\n" +
                "</TEXT>\n";
    }
    private static String getText5(){
        return "<TEXT> Problems in Current Inter-Layer Film Flattening Technology \n" +
                "\n" +
                "Flattening technology                 Fundamental problems \n" +
                "\n" +
                "SOG coating application technology    (1) SOG material which does not need \n" +
                "                                      etch-back and which can coat thick film \n" +
                "                                      has not been developed \n" +
                "\n" +
                "                                      Organic SOG + etch-back process \n" +
                "\n" +
                "                                      Complication of process, increase in \n" +
                "                                      dust generation potential \n" +
                "\n" +
                "                                      (2) Reduction of wiring space has made \n" +
                "                                      it difficult to assure inflow domain in \n" +
                "                                      SOG coating \n" +
                "\n" +
                "                                      Insufficient resolution of wiring height \n" +
                "                                      differences due to denseness/coarseness \n" +
                "                                      of wiring patterns (Figure 2) \n" +
                "\n" +
                "O[.sup]3[/]-TEOS                       (1) Flattening by plugging is possible \n" +
                "                                      only for specific wiring spaces \n" +
                "\n" +
                "                                      Wiring height differences cannot be \n" +
                "                                      overcome (Restrictions in design rule \n" +
                "                                      such as space width increases) \n" +
                "\n" +
                "                                      Combination with other processes is \n" +
                "                                      required, causing complication of \n" +
                "                                      processes \n" +
                "\n" +
                "Sputter-etching combined technology   (1) Plugging characteristics deteriorate \n" +
                "                                      following the reduction of wiring space \n" +
                "                                      (Figure 3 [not reproduced]) \n" +
                "\n" +
                "                                      The repetition count of SiO film \n" +
                "                                      fabrication and sputter-etching should \n" +
                "                                      be increased \n" +
                "\n" +
                "                                      Process is complicated and the \n" +
                "                                      productivity drops greatly \n" +
                "\n" +
                "                                      (2) Only the inclination angle of height \n" +
                "                                      difference can be reduced (45כ) but the \n" +
                "                                      altitude difference cannot be overcome \n" +
                "\n" +
                "                                      Combination with other processes is \n" +
                "                                      required, causing complication of \n" +
                "                                      processes \n" +
                "\n" +
                "\n" +
                "  The flattening technology used most widely at present is the \n" +
                "technology combining SOG-coated film with etch-back, which \n" +
                "provides a flatness with a practically permissible level. </TEXT>";
    }


}

