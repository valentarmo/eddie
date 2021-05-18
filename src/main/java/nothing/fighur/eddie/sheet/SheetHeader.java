package nothing.fighur.eddie.sheet;

public interface SheetHeader {
    /**
     * Update the header's title. The title is drawn at the header's middle position
     * @param title the title. Will put whatever fits in the assigned section.
     */
    void updateTitle(String title);

    /**
     * Update the header's subtitle. The subtitle is drawn at the header's leftmost position
     * @param subtitle the subtitle. Will put whatever fits in the assigned section.
     */
    void updateSubtitle(String subtitle);

    /**
     * Update the logo. The logo is drawn at the header's rightmost position.
     * @param logo character based logo. Will put whatever fits in the assigned section.
     */
    void updateLogo(String logo);
}
