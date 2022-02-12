package com.sysect.smartbuy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sysect.smartbuy.IntegrationTest;
import com.sysect.smartbuy.domain.FileInfo;
import com.sysect.smartbuy.repository.FileInfoRepository;
import com.sysect.smartbuy.service.dto.FileInfoDTO;
import com.sysect.smartbuy.service.mapper.FileInfoMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FileInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileInfoResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/file-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileInfoMockMvc;

    private FileInfo fileInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileInfo createEntity(EntityManager em) {
        FileInfo fileInfo = new FileInfo()
            .fileName(DEFAULT_FILE_NAME)
            .originalFileName(DEFAULT_ORIGINAL_FILE_NAME)
            .fileSize(DEFAULT_FILE_SIZE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return fileInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileInfo createUpdatedEntity(EntityManager em) {
        FileInfo fileInfo = new FileInfo()
            .fileName(UPDATED_FILE_NAME)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return fileInfo;
    }

    @BeforeEach
    public void initTest() {
        fileInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createFileInfo() throws Exception {
        int databaseSizeBeforeCreate = fileInfoRepository.findAll().size();
        // Create the FileInfo
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(fileInfo);
        restFileInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeCreate + 1);
        FileInfo testFileInfo = fileInfoList.get(fileInfoList.size() - 1);
        assertThat(testFileInfo.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testFileInfo.getOriginalFileName()).isEqualTo(DEFAULT_ORIGINAL_FILE_NAME);
        assertThat(testFileInfo.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testFileInfo.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testFileInfo.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createFileInfoWithExistingId() throws Exception {
        // Create the FileInfo with an existing ID
        fileInfo.setId(1L);
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(fileInfo);

        int databaseSizeBeforeCreate = fileInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFileInfos() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);

        // Get all the fileInfoList
        restFileInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].originalFileName").value(hasItem(DEFAULT_ORIGINAL_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getFileInfo() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);

        // Get the fileInfo
        restFileInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, fileInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileInfo.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.originalFileName").value(DEFAULT_ORIGINAL_FILE_NAME))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFileInfo() throws Exception {
        // Get the fileInfo
        restFileInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFileInfo() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);

        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();

        // Update the fileInfo
        FileInfo updatedFileInfo = fileInfoRepository.findById(fileInfo.getId()).get();
        // Disconnect from session so that the updates on updatedFileInfo are not directly saved in db
        em.detach(updatedFileInfo);
        updatedFileInfo
            .fileName(UPDATED_FILE_NAME)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(updatedFileInfo);

        restFileInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
        FileInfo testFileInfo = fileInfoList.get(fileInfoList.size() - 1);
        assertThat(testFileInfo.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileInfo.getOriginalFileName()).isEqualTo(UPDATED_ORIGINAL_FILE_NAME);
        assertThat(testFileInfo.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFileInfo.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFileInfo.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingFileInfo() throws Exception {
        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();
        fileInfo.setId(count.incrementAndGet());

        // Create the FileInfo
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(fileInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileInfo() throws Exception {
        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();
        fileInfo.setId(count.incrementAndGet());

        // Create the FileInfo
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(fileInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileInfo() throws Exception {
        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();
        fileInfo.setId(count.incrementAndGet());

        // Create the FileInfo
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(fileInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileInfoWithPatch() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);

        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();

        // Update the fileInfo using partial update
        FileInfo partialUpdatedFileInfo = new FileInfo();
        partialUpdatedFileInfo.setId(fileInfo.getId());

        partialUpdatedFileInfo.fileSize(UPDATED_FILE_SIZE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restFileInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileInfo))
            )
            .andExpect(status().isOk());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
        FileInfo testFileInfo = fileInfoList.get(fileInfoList.size() - 1);
        assertThat(testFileInfo.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testFileInfo.getOriginalFileName()).isEqualTo(DEFAULT_ORIGINAL_FILE_NAME);
        assertThat(testFileInfo.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFileInfo.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFileInfo.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateFileInfoWithPatch() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);

        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();

        // Update the fileInfo using partial update
        FileInfo partialUpdatedFileInfo = new FileInfo();
        partialUpdatedFileInfo.setId(fileInfo.getId());

        partialUpdatedFileInfo
            .fileName(UPDATED_FILE_NAME)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restFileInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileInfo))
            )
            .andExpect(status().isOk());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
        FileInfo testFileInfo = fileInfoList.get(fileInfoList.size() - 1);
        assertThat(testFileInfo.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileInfo.getOriginalFileName()).isEqualTo(UPDATED_ORIGINAL_FILE_NAME);
        assertThat(testFileInfo.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFileInfo.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFileInfo.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingFileInfo() throws Exception {
        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();
        fileInfo.setId(count.incrementAndGet());

        // Create the FileInfo
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(fileInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileInfo() throws Exception {
        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();
        fileInfo.setId(count.incrementAndGet());

        // Create the FileInfo
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(fileInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileInfo() throws Exception {
        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();
        fileInfo.setId(count.incrementAndGet());

        // Create the FileInfo
        FileInfoDTO fileInfoDTO = fileInfoMapper.toDto(fileInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileInfo() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);

        int databaseSizeBeforeDelete = fileInfoRepository.findAll().size();

        // Delete the fileInfo
        restFileInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        assertThat(fileInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
