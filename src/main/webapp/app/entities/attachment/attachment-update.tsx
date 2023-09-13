import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDodument } from 'app/shared/model/dodument.model';
import { getEntities as getDoduments } from 'app/entities/dodument/dodument.reducer';
import { IAttachment } from 'app/shared/model/attachment.model';
import { getEntity, updateEntity, createEntity, reset } from './attachment.reducer';

export const AttachmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const doduments = useAppSelector(state => state.dodument.entities);
  const attachmentEntity = useAppSelector(state => state.attachment.entity);
  const loading = useAppSelector(state => state.attachment.loading);
  const updating = useAppSelector(state => state.attachment.updating);
  const updateSuccess = useAppSelector(state => state.attachment.updateSuccess);

  const handleClose = () => {
    navigate('/attachment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDoduments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...attachmentEntity,
      ...values,
      dodument: doduments.find(it => it.id.toString() === values.dodument.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...attachmentEntity,
          dodument: attachmentEntity?.dodument?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="odiseyDocApp.attachment.home.createOrEditLabel" data-cy="AttachmentCreateUpdateHeading">
            <Translate contentKey="odiseyDocApp.attachment.home.createOrEditLabel">Create or edit a Attachment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="attachment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('odiseyDocApp.attachment.description')}
                id="attachment-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedBlobField
                label={translate('odiseyDocApp.attachment.file')}
                id="attachment-file"
                name="file"
                data-cy="file"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                id="attachment-dodument"
                name="dodument"
                data-cy="dodument"
                label={translate('odiseyDocApp.attachment.dodument')}
                type="select"
              >
                <option value="" key="0" />
                {doduments
                  ? doduments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/attachment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AttachmentUpdate;
